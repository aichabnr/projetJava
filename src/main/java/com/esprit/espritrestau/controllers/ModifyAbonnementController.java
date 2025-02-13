package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.services.AbonnementService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date; // SQL Date
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ModifyAbonnementController {

    @FXML
    private DatePicker dateDebutField;

    @FXML
    private DatePicker dateFinField;

    @FXML
    private TextField soldeField;

    @FXML
    private MenuButton consommateurMenuButton;

    private AbonnementService abonnementService; // Declare without initialization
    private Abonnement abonnement;
    private int idConsomateur;

    public ModifyAbonnementController() {
        try {
            this.abonnementService = new AbonnementService();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
        dateDebutField.setValue(convertToLocalDate(abonnement.getDateDebut()));
        dateFinField.setValue(convertToLocalDate(abonnement.getDateFin()));
        soldeField.setText(String.valueOf(abonnement.getSolde()));
        loadConsumers();
    }

    private void loadConsumers() {
        if (abonnementService != null) {
            List<Consommateur> consommateurs = abonnementService.getAllConsommateurs();
            for (Consommateur consommateur : consommateurs) {
                MenuItem menuItem = new MenuItem(consommateur.getNom());
                menuItem.setOnAction(event -> {
                    idConsomateur = consommateur.getId();
                    consommateurMenuButton.setText(consommateur.getNom());
                });
                consommateurMenuButton.getItems().add(menuItem);
            }
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @FXML
    public void saveAbonnement() {
        try {
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            double solde = Double.parseDouble(soldeField.getText());

            java.sql.Date sqlDateDebut = java.sql.Date.valueOf(dateDebut);
            java.sql.Date sqlDateFin = java.sql.Date.valueOf(dateFin);

            Abonnement updatedAbonnement = new Abonnement(abonnement.getId(), sqlDateDebut, sqlDateFin, solde, idConsomateur);
            abonnementService.updateAbonnement(updatedAbonnement);

            Stage stage = (Stage) dateDebutField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) dateDebutField.getScene().getWindow();
        stage.close();
    }
}