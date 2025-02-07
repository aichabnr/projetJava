package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.services.AbonnementService;
import com.esprit.espritrestau.services.ConsommateurService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private TableColumn<Abonnement, String> colConsommateur; // Updated type to String for consumer name

    @FXML
    private TableColumn<Abonnement, String> actionColumn;

    @FXML
    private MenuButton consommateurInput;

    @FXML
    private Button createAbonButton;

    @FXML
    private DatePicker dateDebutInput;

    @FXML
    private DatePicker dateFinInput;

    @FXML
    private TextField soldeInput;

    private final AbonnementService abonnementService;
    private final ConsommateurService consommateurService;
    private int selectedConsommateurId;

    public AbonnementController() throws SQLException {
        this.abonnementService = new AbonnementService();
        this.consommateurService = new ConsommateurService();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));

        // Configure the consumer column to show names
        colConsommateur.setCellValueFactory(cellData -> {
            Abonnement abonnement = cellData.getValue();
            return new SimpleStringProperty(abonnement.getNomConsomateur() + " " + abonnement.getPrenomConsomateur());
        });

        setupActionColumn();
        loadAbonnements();
        loadConsommateurs();
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<TableColumn<Abonnement, String>, TableCell<Abonnement, String>>() {
            @Override
            public TableCell<Abonnement, String> call(TableColumn<Abonnement, String> param) {
                return new TableCell<Abonnement, String>() {
                    final Button modifyButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            Abonnement abonnement = getTableView().getItems().get(getIndex());
                            modifyButton.setOnAction(event -> modifyAbonnement(abonnement));
                            deleteButton.setOnAction(event -> deleteAbonnement(abonnement));
                            HBox hbox = new HBox(modifyButton, deleteButton);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void modifyAbonnement(Abonnement abonnement) {
        try {
            URL fxmlUrl = getClass().getResource("/com/esprit/espritrestau/ModifyAbonnement.fxml");
            if (fxmlUrl == null) {
                throw new IllegalArgumentException("FXML file not found: ModifyAbonnement.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            ModifyAbonnementController controller = loader.getController();
            controller.setAbonnement(abonnement);

            Stage stage = new Stage();
            stage.setTitle("Modifier Abonnement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur s'est produite lors de l'ouverture du formulaire de modification.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }
    private void deleteAbonnement(Abonnement abonnement) {
        try {
            abonnementService.deleteAbonnement(abonnement.getId());
            loadAbonnements(); // Refresh the table after deletion
            showAlert("Success", "Abonnement supprimé avec succès.");
        } catch (Exception e) {
            showAlert("Error", "Une erreur s'est produite lors de la suppression.");
        }
    }

    private void loadAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementService.getAllAbonnements();
            ObservableList<Abonnement> observableList = FXCollections.observableArrayList(abonnements);
            tableAbonnements.setItems(observableList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConsommateurs() {
        try {
            List<Consommateur> consommateurs = consommateurService.getAllConsommateurs();
            for (Consommateur consommateur : consommateurs) {
                MenuItem item = new MenuItem(consommateur.getNom() + " " + consommateur.getPrenom());
                item.setOnAction(event -> handleConsommateurSelection(consommateur));
                consommateurInput.getItems().add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur s'est produite lors du chargement des consommateurs.");
        }
    }

    private void handleConsommateurSelection(Consommateur consommateur) {
        selectedConsommateurId = consommateur.getId();
        consommateurInput.setText(consommateur.getNom() + " " + consommateur.getPrenom());
    }

    @FXML
    void addAbonnement(ActionEvent event) {
        try {
            if (dateDebutInput.getValue() == null || dateFinInput.getValue() == null) {
                showAlert("Error", "Veuillez sélectionner une date de début et une date de fin.");
                return;
            }

            Date dateDebut = Date.valueOf(dateDebutInput.getValue());
            Date dateFin = Date.valueOf(dateFinInput.getValue());

            double solde;
            try {
                solde = Double.parseDouble(soldeInput.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Veuillez entrer un solde valide.");
                return;
            }

            Abonnement newAbonnement = new Abonnement(0, dateDebut, dateFin, solde, selectedConsommateurId);
            abonnementService.addAbonnement(newAbonnement);
            loadAbonnements();
            showAlert("Success", "Abonnement ajouté avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur s'est produite lors de l'ajout de l'abonnement.");
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