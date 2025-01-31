package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.entities.TPA;
import com.esprit.espritrestau.services.PersonneService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent; // Import ActionEvent

import java.sql.SQLException;

public class ConsommateurController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtType;

    private PersonneService personneService;

    public ConsommateurController() throws SQLException {
        this.personneService = new PersonneService();
    }

    // Updated method signature to accept ActionEvent instead of MouseEvent
    @FXML
    public void addConsommateur(ActionEvent event) {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String tel = txtTel.getText();
        String password = txtPassword.getText();
        String type = txtType.getText();
        int id  = (int) (Math.random() % 99999) + 1;

        Consommateur consommateur = new Consommateur(id, nom, prenom, tel, password);
        consommateur.setType(TPA.ETUDIANT);

        boolean result = personneService.savePersonne(consommateur);
        if (result) {
            showAlert("Succès", "Consommateur ajouté avec succès.");
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du consommateur.");
        }
    }

    // Updated method signature to accept ActionEvent instead of MouseEvent
    @FXML
    public void updateConsommateur(ActionEvent event) {
        int id  = (int) (Math.random() % 99999) + 1;
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String tel = txtTel.getText();
        String password = txtPassword.getText();
        String type = txtType.getText();

        Consommateur consommateur = new Consommateur(id, nom, prenom, tel, password);
        consommateur.setId(1); // Just an example. You should get the ID from the selected row

        boolean result = personneService.updatePersonne(consommateur);
        if (result) {
            showAlert("Succès", "Consommateur mis à jour avec succès.");
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour.");
        }
    }

    // Updated method signature to accept ActionEvent instead of MouseEvent
    @FXML
    public void deleteConsommateur(ActionEvent event) {
        int id = 1; // Just an example. You should get the ID from the selected row

        boolean result = personneService.deletePersonne(id);
        if (result) {
            showAlert("Succès", "Consommateur supprimé avec succès.");
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la suppression.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
