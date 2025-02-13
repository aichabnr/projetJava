package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.entities.TPA;
import com.esprit.espritrestau.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsommateurService implements IConsommateur<Consommateur> {
    private final Connection connection;

    public ConsommateurService() throws SQLException {
        this.connection = DataSource.getConnection();
    }


    @Override
    public List<Consommateur> getAllConsommateurs() {
        List<Consommateur> consommateurs = new ArrayList<>();
        String query = "SELECT * FROM consommateur"; // Adaptez cette requête
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Consommateur consommateur = new Consommateur(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getInt("tel"),
                        resultSet.getString("password"),
                        TPA.valueOf(resultSet.getString("type")) // Assurez-vous que le type est bien récupéré
                );
                consommateurs.add(consommateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consommateurs;
    }


}