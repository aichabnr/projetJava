package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.services.PersonneService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import java.sql.SQLException;

public class DashboardController {


    @FXML
    private Arc consumerArc;

    @FXML
    private Label totalCountLabel;

    @FXML
    private Label consumerLabel;

    @FXML
    private Label employeeLabel;


    private PersonneService personneService;

    public DashboardController() throws SQLException {
        try {
            this.personneService = new PersonneService();
        } catch (SQLException e) {
            System.err.println("Error initializing PersonneService: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        if (personneService != null) {
            try {
                long totalConsumers = personneService.getTotalConsumers();
                long totalEmployees = personneService.getTotalEmployees();
                long total = totalConsumers + totalEmployees;

                totalCountLabel.setText(String.valueOf(total));
                consumerLabel.setText("Consommateurs: " + totalConsumers);
                employeeLabel.setText("Employés: " + totalEmployees);

                double consumerPercentage = (double) totalConsumers / total;

                consumerArc.setStartAngle(90);
                consumerArc.setLength(360 * consumerPercentage);

            } catch (SQLException e) {
                System.err.println("Error fetching counts: " + e.getMessage());
                totalCountLabel.setText("Erreur");
                consumerLabel.setText("Erreur");
                employeeLabel.setText("Erreur");

            }
        } else {
            totalCountLabel.setText("Error: Service unavailable");
            consumerLabel.setText("Error: Service unavailable");
            employeeLabel.setText("Error: Service unavailable");
        }
    }
}