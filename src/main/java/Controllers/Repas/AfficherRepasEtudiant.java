package Controllers.Repas;

import Entites.Repas;
import Services.serviceRepas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherRepasEtudiant {

    @FXML
    private Button btnSearch;

    @FXML
    private Button chatbot;

    @FXML
    private Button chercher;

    @FXML
    private TableColumn<Repas, Void> colCout;

    @FXML
    private TableColumn<Repas, Void> colDate;

    @FXML
    private TableColumn<Repas, Void> colId;

    @FXML
    private TableColumn<Repas, Void> colNbr;

    @FXML
    private TableColumn<Repas, Void> colNom;

    @FXML
    private TableView<Repas> tableStudent;

    @FXML
    private TitledPane titledPane;

    @FXML
    private TextField txtSearch;

    @FXML
    void initialize() {
        try {
            serviceRepas repasService = new serviceRepas();

            // Charger tous les repas
            List<Repas> repasList = repasService.getAll();
            ObservableList<Repas> obse = FXCollections.observableList(repasList);
            tableStudent.setItems(obse);

            // Définir les valeurs des colonnes
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colNbr.setCellValueFactory(new PropertyValueFactory<>("nbRepas"));
            colCout.setCellValueFactory(new PropertyValueFactory<>("cout"));



        } catch (Exception e) {
            // Gérer les erreurs
            System.err.println("Erreur lors du chargement des repas : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Fonctionnalité pour aller à la page du chatbot
    @FXML
    private void gochat(ActionEvent event) {
        try {
            // Charger la nouvelle interface Chatbot.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/ChatBot.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et la remplacer par la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Chatbot");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Fonction de retour
    @FXML
    private void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/AfficherRepas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Repas");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page Repas.");
        }
    }
    @FXML
    private void searchRepas(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();
        serviceRepas serviceRepas = new serviceRepas();

        try {
            List<Repas> Repas;
            if (searchTerm.isEmpty()) {
                Repas = serviceRepas.getAll();
            } else {
                Repas = serviceRepas.chercherParNom(searchTerm);
            }

            ObservableList<Repas> observableList = FXCollections.observableArrayList(Repas);

            tableStudent.setItems(observableList);

        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des Repas : " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    void openProposition(ActionEvent event) {
        try {
            // Charger le fichier FXML de la proposition
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/afficherPropositions.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la proposition.");
        }
    }

    @FXML
    void openReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamations.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Reclamation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la Reclamation.");
        }
    }
    @FXML
    void openRepas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/AfficherRepasEtudiant.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Reclamation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la repas.");
        }
    }
}

