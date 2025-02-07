package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Presence;
import com.esprit.espritrestau.entities.Repas;
import com.esprit.espritrestau.entities.Consommateur; // Import Consommateur entity
import com.esprit.espritrestau.services.LignePresenceService;
import com.esprit.espritrestau.utils.DataSource;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class LignePresenceController {

    @FXML
    private TableView<Presence> presenceTableView;

    @FXML
    private TableColumn<Presence, Integer> idColumn;

    @FXML
    private TableColumn<Presence, Date> dateColumn;

    @FXML
    private TableColumn<Presence, Integer> idRepasColumn;

    @FXML
    private TableColumn<Presence, Integer> idConsomateurColumn;

    @FXML
    private ComboBox<Repas> repasComboBox;

    @FXML
    private ComboBox<Consommateur> consommateurComboBox;

    @FXML
    private DatePicker dateInput;

    @FXML
    private Button addButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Button deleteButton;
    private LignePresenceService service;
    private Connection connection;

    @FXML
    public void initialize() {
        try {
            service = new LignePresenceService();
            connection = DataSource.getConnection();
            loadRepasData();
            loadConsommateurData();
            loadPresenceData();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        idRepasColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdRepas()).asObject());
        idConsomateurColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdConsomateur()).asObject());

        adjustColumnWidths();
        presenceTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                modifyButton.setVisible(true);
                deleteButton.setVisible(true);
            } else {
                modifyButton.setVisible(false);
                deleteButton.setVisible(false);
            }
        });
    }

    private void loadRepasData() {
        List<Repas> repasList = new ArrayList<>();
        String query = "SELECT * FROM repas";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Repas repas = new Repas(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("nom-repas"),
                        rs.getInt("nbrRepas"),
                        rs.getDouble("cout")
                );
                repasList.add(repas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        repasComboBox.getItems().setAll(repasList);
    }
    @FXML
    public void handleModifyButton() {
        Presence selectedPresence = presenceTableView.getSelectionModel().getSelectedItem();
        if (selectedPresence != null) {
            repasComboBox.setValue(service.getRepasById(selectedPresence.getIdRepas()));
            consommateurComboBox.setValue(service.getConsommateurById(selectedPresence.getIdConsomateur()));

            // Convert java.sql.Date to LocalDate
            LocalDate localDate = selectedPresence.getDate().toLocalDate();
            dateInput.setValue(localDate);

            // Add a confirmation dialog for the user to confirm the modification
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Modification");
            alert.setHeaderText("Are you sure you want to modify this entry?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Save the modified entry
                int id = selectedPresence.getId();
                LocalDate newDate = dateInput.getValue();
                int newIdRepas = repasComboBox.getValue().getId();
                int newIdConsomateur = consommateurComboBox.getValue().getId();

                if (newDate != null) {
                    service.update(id, java.sql.Date.valueOf(newDate), newIdRepas, newIdConsomateur);
                    loadPresenceData();
                    showAlert("Success", "Presence successfully modified!");
                } else {
                    showAlert("Error", "Date cannot be null!");
                }
            }
        }
    }
    @FXML
    public void handleDeleteButton() {
        Presence selectedPresence = presenceTableView.getSelectionModel().getSelectedItem();
        if (selectedPresence != null) {
            service.delete(selectedPresence.getId());
            loadPresenceData(); // Refresh the table
            showAlert("Success", "Presence successfully deleted!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadConsommateurData() {
        List<Consommateur> consommateurList = new ArrayList<>();
        String query = "SELECT * FROM consommateur";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Consommateur consommateur = new Consommateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("tel"),
                        rs.getString("password"),
                        null
                );
                consommateurList.add(consommateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        consommateurComboBox.getItems().setAll(consommateurList);
    }

    private void loadPresenceData() {
        List<Presence> presenceList = service.getAll();
        presenceTableView.getItems().setAll(presenceList);
    }

    private void adjustColumnWidths() {

        presenceTableView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                presenceTableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                    double totalWidth = newWidth.doubleValue();
                    idColumn.setPrefWidth(totalWidth * 0.25);
                    dateColumn.setPrefWidth(totalWidth * 0.25);
                    idRepasColumn.setPrefWidth(totalWidth * 0.25);
                    idConsomateurColumn.setPrefWidth(totalWidth * 0.25);
                });
            }
        });
    }


    @FXML
    public void handleAddButton() {
        Repas selectedRepas = repasComboBox.getValue();
        Consommateur selectedConsommateur = consommateurComboBox.getValue();
        LocalDate localDate = dateInput.getValue();

        if (selectedRepas == null || selectedConsommateur == null || localDate == null) {
            showAlert("Error", "Fill all fields");
            return;
        }

        int idRepas = selectedRepas.getId();
        int idConsomateur = selectedConsommateur.getId();
        Date date = java.sql.Date.valueOf(localDate);

        Presence newPresence = new Presence();
        newPresence.setIdRepas(idRepas);
        newPresence.setIdConsomateur(idConsomateur);
        newPresence.setDate(date);
        service.add(newPresence);
        loadPresenceData();
    }
}