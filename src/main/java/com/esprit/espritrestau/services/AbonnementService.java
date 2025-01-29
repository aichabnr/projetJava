package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService implements IAbonnement<Abonnement> {

    private final Connection connection;

    public AbonnementService() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void addAbonnement(Abonnement abonnement) {
        String query = "INSERT INTO abonnement (id, dateDebut, dateFin, solde, idConsomateur) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, abonnement.getId());
            statement.setDate(2, new java.sql.Date(abonnement.getDateDebut().getTime()));
            statement.setDate(3, new java.sql.Date(abonnement.getDateFin().getTime()));
            statement.setDouble(4, abonnement.getSolde());
            statement.setInt(5, abonnement.getIdConsomateur());
            statement.executeUpdate();
            System.out.println("Abonnement added: " + abonnement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Abonnement getAbonnementById(int id) {
        String query = "SELECT * FROM abonnement WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Abonnement(
                        resultSet.getInt("id"),
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getDouble("solde"),
                        resultSet.getInt("idConsomateur")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Abonnement> getAllAbonnements() {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Abonnement abonnement = new Abonnement(
                        resultSet.getInt("id"),
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getDouble("solde"),
                        resultSet.getInt("idConsomateur")
                );
                abonnements.add(abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnements.stream().toList();
    }

    @Override
    public void updateAbonnement(Abonnement abonnement) {
        String query = "UPDATE abonnement SET dateDebut = ?, dateFin = ?, solde = ?, idConsomateur = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(abonnement.getDateDebut().getTime()));
            statement.setDate(2, new java.sql.Date(abonnement.getDateFin().getTime()));
            statement.setDouble(3, abonnement.getSolde());
            statement.setInt(4, abonnement.getIdConsomateur());
            statement.setInt(5, abonnement.getId());
            statement.executeUpdate();
            System.out.println("Abonnement updated: " + abonnement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAbonnement(int id) {
        String query = "DELETE FROM abonnement WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Abonnement with ID " + id + " deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
