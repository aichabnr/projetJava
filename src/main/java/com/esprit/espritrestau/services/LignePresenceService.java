package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.Presence;
import com.esprit.espritrestau.entities.Repas; // Import Repas entity
import com.esprit.espritrestau.entities.Consommateur; // Import Consommateur entity
import com.esprit.espritrestau.utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LignePresenceService implements ILignePresence<Presence> {

    private final Connection connection;

    public LignePresenceService() throws SQLException {
        this.connection = DataSource.getConnection();
    }

    @Override
    public void add(Presence p) {
        String query = "INSERT INTO ligpresence (date, idRepas, idConsomateur) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, new java.sql.Date(p.getDate().getTime()));
            pstmt.setInt(2, p.getIdRepas());
            pstmt.setInt(3, p.getIdConsomateur());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1)); // Set the generated ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Presence> getById(int id) {
        String query = "SELECT * FROM ligpresence WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Presence presence = new Presence(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getInt("idRepas"),
                        rs.getInt("idConsomateur")
                );
                return Optional.of(presence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Presence> getAll() {
        List<Presence> presenceList = new ArrayList<>();
        String query = "SELECT * FROM ligpresence";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Presence presence = new Presence(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getInt("idRepas"),
                        rs.getInt("idConsomateur")
                );
                presenceList.add(presence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return presenceList;
    }

    @Override
    public boolean update(int id, Date date, int idRepas, int idConsomateur) {
        String query = "UPDATE ligpresence SET date = ?, idRepas = ?, idConsomateur = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            pstmt.setInt(2, idRepas);
            pstmt.setInt(3, idConsomateur);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM ligpresence WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Repas getRepasById(int id) {
        String query = "SELECT * FROM repas WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Repas(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("nom-repas"),
                        rs.getInt("nbrRepas"),
                        rs.getDouble("cout")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Consommateur getConsommateurById(int id) {
        String query = "SELECT * FROM consommateur WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Consommateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("tel"),
                        rs.getString("password"),
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Presence> getPresencesBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Presence> presenceList = new ArrayList<>();
        String query = "SELECT * FROM ligpresence WHERE date BETWEEN ? AND ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Presence presence = new Presence(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getInt("idRepas"),
                        rs.getInt("idConsomateur")
                );
                presenceList.add(presence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return presenceList;
    }
}