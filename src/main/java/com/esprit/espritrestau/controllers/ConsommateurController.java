package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Consommateur;
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
import java.util.Optional;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

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

        setupActionColumn();

        tableConsommateurs.setItems(consommateurList);

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

            List<Consommateur> consommateurs = personneService.getAllConsommateurs();

            consommateurList.clear();
            consommateurList.addAll(consommateurs);
            System.out.println("Number of consumers in list: " + consommateurList.size());
            tableConsommateurs.setItems(consommateurList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des consommateurs.");
        }
    }


    private void modifyConsommateur(Consommateur consommateur) {
        // Créer le dialogue
        Dialog<Consommateur> dialog = new Dialog<>();
        dialog.setTitle("Modifier Consommateur");
        dialog.setHeaderText("Modifier les informations du consommateur");

        // Définir les boutons
        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, ButtonType.CANCEL);

        // Créer le layout (GridPane pour une disposition plus propre)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Créer les champs
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        nomField.setText(consommateur.getNom());

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        prenomField.setText(consommateur.getPrenom());

        TextField telField = new TextField();
        telField.setPromptText("Téléphone");
        telField.setText(consommateur.getTel());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setText(consommateur.getPassword());

        ComboBox<TPA> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(TPA.values());
        typeComboBox.setValue(consommateur.getType());

        // Ajouter les champs au layout
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2);
        grid.add(telField, 1, 2);
        grid.add(new Label("Mot de passe:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Type:"), 0, 4);
        grid.add(typeComboBox, 1, 4);

        // Définir le contenu du dialogue
        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat quand le bouton "Enregistrer" est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                // Mettre à jour l'objet Consommateur avec les valeurs des champs
                consommateur.setNom(nomField.getText());
                consommateur.setPrenom(prenomField.getText());
                consommateur.setTel(telField.getText());
                consommateur.setPassword(passwordField.getText());
                consommateur.setType(typeComboBox.getValue());
                return consommateur;
            }
            return null;
        });

        // Afficher le dialogue et attendre une réponse
        Optional<Consommateur> result = dialog.showAndWait();

        result.ifPresent(updatedConsommateur -> {
            // Mettre à jour le consommateur dans la base de données
            if (personneService.updatePersonne(updatedConsommateur)) {
                loadConsommateurs();
                showAlert("Succès", "Consommateur modifié avec succès.");
            } else {
                showAlert("Erreur", "Erreur lors de la modification du consommateur.");
            }
        });
    }

    private void deleteConsommateur(Consommateur consommateur) {
        boolean result = personneService.deletePersonne(consommateur);
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

