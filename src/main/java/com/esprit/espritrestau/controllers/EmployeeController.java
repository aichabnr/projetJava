package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Employee;
import com.esprit.espritrestau.services.PersonneService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.util.Optional;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, Integer> colId;
    @FXML
    private TableColumn<Employee, String> colNom;
    @FXML
    private TableColumn<Employee, String> colPrenom;
    @FXML
    private TableColumn<Employee, String> colTel;
    @FXML
    private TableColumn<Employee, String> colPassword;
    @FXML
    private TableColumn<Employee, String> colPost;
    @FXML
    private TableColumn<Employee, String> colMatricule;
    @FXML
    private TableColumn<Employee, Void> colActions;

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtTel;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtPost;
    @FXML
    private TextField txtMatricule;

    @FXML
    private Button btnAjouter;

    private PersonneService personneService;
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();


    public EmployeeController() throws SQLException {
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
        colPost.setCellValueFactory(new PropertyValueFactory<>("post"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matriculeSocial"));

        setupActionColumn();  // Initialize the action column

        // Set items in table
        employeeTableView.setItems(employeeList);

        // Load initial data
        loadEmployees();
    }

    private void setupActionColumn() {
        colActions.setCellFactory(new Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                return new TableCell<Employee, Void>() {
                    private final Button modifyButton = new Button("Modifier");
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        modifyButton.setOnAction((ActionEvent event) -> {
                            Employee data = getTableView().getItems().get(getIndex());
                            modifyEmployee(data); // Select the employee for update
                        });
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Employee data = getTableView().getItems().get(getIndex());
                            deleteEmployee(data);
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
                            buttons.setSpacing(5); // Add some spacing between buttons
                            setGraphic(buttons);
                            setText(null);
                        }
                    }
                };
            }
        });
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = personneService.getAllEmployees();
            employeeList.clear();
            employeeList.addAll(employees);
            employeeTableView.setItems(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des employés.");
        }
    }

    @FXML
    public void addEmployee(ActionEvent event) {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String tel = txtTel.getText();
        String password = txtPassword.getText();
        String post = txtPost.getText();
        String matricule = txtMatricule.getText();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || password.isEmpty() || post.isEmpty() || matricule.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Employee employee = new Employee(0, nom, prenom, tel, password, post, matricule);
        boolean result = personneService.savePersonne(employee);
        if (result) {
            loadEmployees();
            clearFields();
            showAlert("Succès", "Employé ajouté avec succès.");
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout de l'employé.");
        }
    }


    private void modifyEmployee(Employee employee) {
        // Créer le dialogue
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Modifier Employé");
        dialog.setHeaderText("Modifier les informations de l'employé");

        // Créer les boutons
        ButtonType enregistrerButtonType = new ButtonType("Enregistrer", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enregistrerButtonType, ButtonType.CANCEL);

        // Créer le layout (GridPane pour une disposition plus propre)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Créer les champs
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        nomField.setText(employee.getNom());

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        prenomField.setText(employee.getPrenom());

        TextField telField = new TextField();
        telField.setPromptText("Téléphone");
        telField.setText(employee.getTel());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setText(employee.getPassword());
        TextField postField = new TextField();
        postField.setPromptText("Post");
        postField.setText(employee.getPost());

        TextField matriculeField = new TextField();
        matriculeField.setPromptText("Matricule");
        matriculeField.setText(employee.getMatriculeSocial());


        // Ajouter les champs au layout
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2);
        grid.add(telField, 1, 2);
        grid.add(new Label("Mot de passe:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Poste:"), 0, 4);
        grid.add(postField, 1, 4);
        grid.add(new Label("Matricule:"), 0, 5);
        grid.add(matriculeField, 1, 5);

        // Définir le contenu du dialogue
        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat quand le bouton "Enregistrer" est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enregistrerButtonType) {
                // Mettre à jour l'objet Consommateur avec les valeurs des champs
                employee.setNom(nomField.getText());
                employee.setPrenom(prenomField.getText());
                employee.setTel(telField.getText());
                employee.setPassword(passwordField.getText());
                employee.setPost(postField.getText());
                employee.setMatriculeSocial(matriculeField.getText());
                return employee;
            }
            return null;
        });

        // Afficher le dialogue et attendre une réponse
        Optional<Employee> result = dialog.showAndWait();

        result.ifPresent(updatedEmployee -> {
            // Mettre à jour le consommateur dans la base de données
            if (personneService.updatePersonne(updatedEmployee)) {
                loadEmployees();
                showAlert("Succès", "Employe modifié avec succès.");
            } else {
                showAlert("Erreur", "Erreur lors de la modification du consommateur.");
            }
        });
    }

    private void deleteEmployee(Employee employee) {
        boolean result = personneService.deletePersonne(employee);
        if (result) {
            loadEmployees();
            showAlert("Succès", "Employé supprimé avec succès.");
        } else {
            showAlert("Erreur", "Erreur lors de la suppression de l'employé.");
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        txtTel.clear();
        txtPassword.clear();
        txtPost.clear();
        txtMatricule.clear();
        btnAjouter.setDisable(false);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}