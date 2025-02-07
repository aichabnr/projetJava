package Controllers.Reclamation;

import Entites.Proposition;
import Entites.Reclamation;
import Services.ServiceReclamation;
import Services.serviceProposition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.SQLException;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjouterReclamation {
    @FXML
    private Label label;

    @FXML
    private TextField Description;

    @FXML
    private TextField Objet;
    private Services.ServiceReclamation  serviceReclamation ;
    public AjouterReclamation() {
        this.serviceReclamation = new ServiceReclamation();
    }
    private Date sysDate() {
        return new Date();  // Renvoie la date actuelle
    }
    @FXML
    void save(ActionEvent event) throws SQLException {
        String description = Description.getText();
        String objetText = Objet.getText();
        Date date = sysDate();  // Appel à ta méthode sysDate()

        Reclamation rec1 = new Reclamation(date,description,objetText,1);
        if (description.isEmpty() || objetText.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }
        serviceReclamation.ajouter(rec1); // Appel du service

    }

    @FXML
    private void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamations.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }    }
}
