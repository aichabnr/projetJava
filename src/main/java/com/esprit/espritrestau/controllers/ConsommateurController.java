package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.entities.Personne;
import com.esprit.espritrestau.entities.TPA;
import com.esprit.espritrestau.services.PersonneService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class ConsommateurController {

    @FXML
    private TableView<Consommateur> tableConsommateurs;
    @FXML
    private TableColumn<Consommateur, Integer> colId;
    @FXML
    private TableColumn<Consommateur, String> colNom;
    @FXML
    private TableColumn<Consommateur, String> colPrenom;
    @FXML
    private TableColumn<Consommateur, String> colTel;
    @FXML
    private TableColumn<Consommateur, String> colPassword;
    @FXML
    private TableColumn<Consommateur, TPA> colType;
    @FXML
    private TableColumn<Consommateur, Void> colActions;

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtTel;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private MenuButton menuType;
    @FXML
    private MenuItem menuItemEtudiant;
    @FXML
    private MenuItem menuItemPersonnel;
    @FXML
    private Button btnAjouter;

    private PersonneService personneService;
    private ObservableList<Consommateur> consommateurList = FXCollections.observableArrayList();
    private TPA selectedType = null;

    public ConsommateurController() throws SQLException {
        this.personneService = new PersonneService();
    }

    @FXML
    public void initialize() {
        // Initialize the TableView columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        setupActionColumn();  // Initialize the action column

        // Set items in table
        tableConsommateurs.setItems(consommateurList);

        // Load initial data
        loadConsommateurs();

        menuItemEtudiant = new MenuItem("Etudiant");
        menuItemEtudiant.setOnAction(event -> {
            handleTypeSelection(TPA.ETUDIANT);
            menuType.setText("Etudiant");
        });
        menuType.getItems().add(menuItemEtudiant);

        menuItemPersonnel = new MenuItem("Personnel");
        menuItemPersonnel.setOnAction(event -> {
            handleTypeSelection(TPA.PERSONNEL);
            menuType.setText("Personnel");
        });
        menuType.getItems().add(menuItemPersonnel);
    }

    private void setupActionColumn() {
        colActions.setCellFactory(new Callback<TableColumn<Consommateur, Void>, TableCell<Consommateur, Void>>() {
            @Override
            public TableCell<Consommateur, Void> call(final TableColumn<Consommateur, Void> param) {
                return new TableCell<Consommateur, Void>() {
                    private final Button modifyButton = new Button("Modifier");
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        modifyButton.setOnAction((ActionEvent event) -> {
                            Consommateur data = getTableView().getItems().get(getIndex());
                            modifyConsommateur(data);
                        });
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Consommateur data = getTableView().getItems().get(getIndex());
                            deleteConsommateur(data);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            HBox buttons = new HBox(modifyButton, deleteButton);
                            setGraphic(buttons);
                            setText(null);
                        }
                    }
                };
            }
        });
    }

    private void loadConsommateurs() {
        try {
            System.out.println("Loading Consumers...");
            // Use the new getAllConsommateurs() method
            List<Consommateur> consommateurs = personneService.getAllConsommateurs(); // NEW METHOD

            consommateurList.clear();
            consommateurList.addAll(consommateurs);
            System.out.println("Number of consumers in list: " + consommateurList.size()); //Debugging
            tableConsommateurs.setItems(consommateurList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des consommateurs.");
        }
    }


    private void modifyConsommateur(Consommateur consommateur) {
        txtNom.setText(consommateur.getNom());
        txtPrenom.setText(consommateur.getPrenom());
        txtTel.setText(consommateur.getTel());
        txtPassword.setText(consommateur.getPassword());
        selectedType = consommateur.getType();
        menuType.setText(selectedType == TPA.ETUDIANT ? "Etudiant" : "Personnel");
    }

    private void deleteConsommateur(Consommateur consommateur) {
        boolean result = personneService.deletePersonne(consommateur.getId());
        if (result) {
            loadConsommateurs();
            showAlert("Succès", "Consommateur supprimé avec succès.");
        } else {
            showAlert("Erreur", "Erreur lors de la suppression du consommateur.");
        }
    }

    @FXML
    public void handleTypeSelection(TPA type) {
        selectedType = type;
        menuType.setText(type == TPA.ETUDIANT ? "Etudiant" : "Personnel");
    }

    @FXML
    public void addConsommateur(ActionEvent event) {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String tel = txtTel.getText();
        String password = txtPassword.getText();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || password.isEmpty() || selectedType == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Consommateur consommateur = new Consommateur(0, nom, prenom, tel, password, selectedType);
        boolean result = personneService.savePersonne(consommateur);
        if (result) {
            loadConsommateurs();
            clearFields();
            showAlert("Succès", "Consommateur ajouté avec succès.");
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout du consommateur.");
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        txtTel.clear();
        txtPassword.clear();
        menuType.setText("Type");
        selectedType = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}