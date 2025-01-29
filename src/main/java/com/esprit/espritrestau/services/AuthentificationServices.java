package com.esprit.espritrestau.services;

import com.esprit.espritrestau.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthentificationServices implements IAuthentification {

    @Override
    public boolean login(String email, String password, String type) {
        String query = null;

        if (type.equalsIgnoreCase("Consumer")) {
            query = "SELECT * FROM consommateur WHERE id = ? AND password = ?";
        } else if (type.equalsIgnoreCase("Employee")) {
            query = "SELECT * FROM employee WHERE id = ? AND password = ?";
        } else {
            System.out.println("Invalid user type.");
            return false;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Authentication successful for " + type);
                return true;
            } else {
                System.out.println("Invalid email or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
