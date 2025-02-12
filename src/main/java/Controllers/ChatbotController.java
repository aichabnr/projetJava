package Controllers;

import Utils.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ChatbotController {

    private Map<String, String> responses = new HashMap<>();

    @FXML
    private TextArea chatArea; // L'élément TextArea pour afficher les réponses

    @FXML
    private TextField userInput; // Le champ de texte où l'utilisateur entre sa question



    public ChatbotController() {
        // Le constructeur peut être vide car l'injection de dépendances FXML gère le reste.
    }

    // Cette méthode est appelée lors de l'événement de clic sur le bouton
    @FXML
    public void handleSend() {
        String todayDate = "2025-02-09"; // À remplacer par la date actuelle ou dynamique
        String userQuestion = userInput.getText().toLowerCase(); // Récupère la question de l'utilisateur, en minuscule pour faciliter la comparaison

        // Requête SQL pour récupérer les repas d'aujourd'hui
        String query = "SELECT `nom-repas`, `cout` FROM repas WHERE STR_TO_DATE(date, '%Y-%m-%d') = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Vérifie si la question contient "repas" ou "menu"
            if (userQuestion.contains("repas") || userQuestion.contains("menu")) {
                // Récupérer la connexion de DataSource
                conn = DataSource.getInstance().getConn();

                // Vérifier si la connexion est toujours valide
                if (conn != null && !conn.isClosed()) {
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, todayDate);  // Remplacer par la date dynamique
                    rs = stmt.executeQuery();

                    StringBuilder repasAujourdHui = new StringBuilder();
                    while (rs.next()) {
                        String nom = rs.getString("nom-repas");
                        double cout = rs.getDouble("cout");
                        repasAujourdHui.append(nom).append(" à ").append(cout).append(" DT\n");
                    }

                    if (repasAujourdHui.length() > 0) {
                        responses.put(userQuestion, repasAujourdHui.toString()); // Réponse dynamique basée sur la question
                    } else {
                        responses.put(userQuestion, "Aucun repas n'est prévu pour aujourd'hui.");
                    }
                } else {
                    responses.put(userQuestion, "La connexion à la base de données a échoué.");
                }
            } else {
                // Si la question ne contient pas "repas" ou "menu", on répond par défaut
                responses.put(userQuestion, "Je n'ai pas compris votre question. Vous pouvez demander les repas ou le menu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responses.put(userQuestion, "Une erreur s'est produite lors de la récupération des repas.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Afficher la réponse dans le TextArea
        String response = getResponse(userQuestion);
        chatArea.appendText("Vous: " + userQuestion + "\n");
        chatArea.appendText("Réponse: " + response + "\n");

        // Effacer le champ de texte après l'envoi
        userInput.clear();
    }

    // Récupérer la réponse basée sur la question de l'utilisateur
    public String getResponse(String userInput) {
        return responses.getOrDefault(userInput, "Désolé, je n'ai pas compris la question.");
    }
}
