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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            System.out.println("Please fill in all fields!");
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