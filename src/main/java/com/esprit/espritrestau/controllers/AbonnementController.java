package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.services.AbonnementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class AbonnementController {

    @FXML
    private TableView<Abonnement> tableAbonnements;

    @FXML
    private TableColumn<Abonnement, Integer> colId;

    @FXML
    private TableColumn<Abonnement, String> colDateDebut;

    @FXML
    private TableColumn<Abonnement, String> colDateFin;

    @FXML
    private TableColumn<Abonnement, Double> colSolde;

    @FXML
    private TableColumn<Abonnement, Integer> colIdConsomateur;

    private final AbonnementService abonnementService = new AbonnementService();

    public AbonnementController() throws SQLException {
    }

    @FXML
    public void initialize() {
        // Associer les colonnes aux attributs de l'objet Abonnement
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        colIdConsomateur.setCellValueFactory(new PropertyValueFactory<>("idConsomateur"));

        // Charger les données depuis la base de données
        loadAbonnements();
    }

    private void loadAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementService.getAllAbonnements();
            ObservableList<Abonnement> observableList = FXCollections.observableArrayList(abonnements);
            tableAbonnements.setItems(observableList);
            System.out.println(abonnements.stream().toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }}