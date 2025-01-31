package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Employee;
import com.esprit.espritrestau.services.PersonneService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    @FXML
    private TextField idField, nomField, prenomField, telField, postField, matriculeField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ListView<Employee> employeeListView;

    private PersonneService personneService;

    public EmployeeController() throws SQLException {
        this.personneService = new PersonneService();
    }

    @FXML
    public void saveEmployee(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String tel = telField.getText();
        String post = postField.getText();
        String matricule = matriculeField.getText();
        String password = passwordField.getText();
        int id = Integer.parseInt(idField.getText());

        Employee employee = new Employee(id, nom, prenom, tel, password, post, matricule);
        boolean isSaved = personneService.savePersonne(employee);
        if (isSaved) {
            showAlert("Success", "Employee saved successfully!");
            loadEmployeeList();
        } else {
            showAlert("Error", "Error saving employee.");
        }
    }

    @FXML
    public void updateEmployee(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String tel = telField.getText();
        String post = postField.getText();
        String matricule = matriculeField.getText();
        String password = passwordField.getText();
        int id = Integer.parseInt(idField.getText());

        Employee employee = new Employee(id, nom, prenom, tel, password, post, matricule);
        boolean isUpdated = personneService.updatePersonne(employee);
        if (isUpdated) {
            showAlert("Success", "Employee updated successfully!");
            loadEmployeeList();
        } else {
            showAlert("Error", "Error updating employee.");
        }
    }

    @FXML
    public void deleteEmployee(ActionEvent event) {
        int id = Integer.parseInt(idField.getText());
        boolean isDeleted = personneService.deletePersonne(id);
        if (isDeleted) {
            showAlert("Success", "Employee deleted successfully!");
            loadEmployeeList();
        } else {
            showAlert("Error", "Error deleting employee.");
        }
    }

    private void loadEmployeeList() {
        List<Employee> employees = (List<Employee>) (List<?>) personneService.getAllPersonnes();
        employeeListView.getItems().setAll(employees);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        loadEmployeeList();
    }
}
