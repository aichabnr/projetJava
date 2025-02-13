package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.EspritRestau;
import com.esprit.espritrestau.services.AuthentificationServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private RadioButton consumerRadioButton;

    @FXML
    private RadioButton employeeRadioButton;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    public void initialize() {
        ToggleGroup roleGroup = new ToggleGroup();
        consumerRadioButton.setToggleGroup(roleGroup);
        employeeRadioButton.setToggleGroup(roleGroup);
    }

    @FXML
    void authenticateUser(ActionEvent event) {

        emailField.getStyleClass().remove("error-border");
        passwordField.getStyleClass().remove("error-border");
        errorLabel.setVisible(false);

        boolean hasError = false;

        if (emailField.getText().isEmpty()) {
            emailField.getStyleClass().add("error-border");
            hasError = true;
        }

        if (passwordField.getText().isEmpty()) {
            passwordField.getStyleClass().add("error-border");
            hasError = true;
        }

        if (hasError) {
            errorLabel.setText("Please fill in all fields!");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
            return;
        }

        String email = emailField.getText();
        String password = passwordField.getText();
        String role = "";

        if (consumerRadioButton.isSelected()) {
            role = "Consumer";
        } else if (employeeRadioButton.isSelected()) {
            role = "Employee";
        } else {
            errorLabel.setText("Please select a role!");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
            return;
        }

        AuthentificationServices authServices = new AuthentificationServices();

        if (authServices.login(email, password, role)) {
            try {
                FXMLLoader loader = new FXMLLoader(EspritRestau.class.getResource("ligne_presence.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load the next page.");
                errorLabel.setTextFill(Color.RED);
                errorLabel.setVisible(true);
            }

        } else {
            errorLabel.setText("Invalid credentials for " + role);
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
        }
    }

}