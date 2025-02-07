package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.entities.Employee;
import com.esprit.espritrestau.entities.Personne;
import com.esprit.espritrestau.entities.TPA;
import com.esprit.espritrestau.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneService {

    private final Connection con;

    public PersonneService() throws SQLException {
        this.con = DatabaseConnection.getConnection();
    }

    public boolean savePersonne(Personne personne) {
        if (personne instanceof Employee) {
            String query = "INSERT INTO employee (nom, prenom, tel, password, post, matriculeSocial) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pre = con.prepareStatement(query)) {
                pre.setString(1, personne.getNom());
                pre.setString(2, personne.getPrenom());
                pre.setString(3, personne.getTel());
                pre.setString(4, personne.getPassword());

                Employee emp = (Employee) personne;
                pre.setString(5, emp.getPost());
                pre.setString(6, emp.getMatriculeSocial());

                pre.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else if (personne instanceof Consommateur) {
            // First, insert into the personne table
            String queryPersonne = "INSERT INTO personne (nom, prenom, Tel, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement prePersonne = con.prepareStatement(queryPersonne, Statement.RETURN_GENERATED_KEYS)) {
                prePersonne.setString(1, personne.getNom());
                prePersonne.setString(2, personne.getPrenom());
                prePersonne.setString(3, personne.getTel());
                prePersonne.setString(4, personne.getPassword());
                prePersonne.executeUpdate();

                // Get the generated ID
                ResultSet generatedKeys = prePersonne.getGeneratedKeys();
                int personneId;
                if (generatedKeys.next()) {
                    personneId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert personne, no ID obtained.");
                }

                // Now, insert into the consommateur table
                String queryConsommateur = "INSERT INTO consommateur (id, type) VALUES (?, ?)";
                try (PreparedStatement preConsommateur = con.prepareStatement(queryConsommateur)) {
                    preConsommateur.setInt(1, personneId); // Use the generated ID
                    preConsommateur.setString(2, ((Consommateur) personne).getType().toString());
                    preConsommateur.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    System.err.println("Error inserting into consommateur: " + e.getMessage());
                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean updatePersonne(Personne personne) {
        if (personne instanceof Employee) {
            String query = "UPDATE employee SET nom = ?, prenom = ?, Tel = ?, password = ?, post = ?, matriculeSocial = ? WHERE id = ?";
            try (PreparedStatement pre = con.prepareStatement(query)) {
                pre.setString(1, personne.getNom());
                pre.setString(2, personne.getPrenom());
                pre.setString(3, personne.getTel());
                pre.setString(4, personne.getPassword());

                Employee emp = (Employee) personne;
                pre.setString(5, emp.getPost());
                pre.setString(6, emp.getMatriculeSocial());

                pre.setInt(7, personne.getId());
                pre.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else if (personne instanceof Consommateur) {
            // First, update the personne table
            String queryPersonne = "UPDATE personne SET nom = ?, prenom = ?, Tel = ?, password = ? WHERE id = ?";
            try (PreparedStatement prePersonne = con.prepareStatement(queryPersonne)) {
                prePersonne.setString(1, personne.getNom());
                prePersonne.setString(2, personne.getPrenom());
                prePersonne.setString(3, personne.getTel());
                prePersonne.setString(4, personne.getPassword());
                prePersonne.setInt(5, personne.getId());
                prePersonne.executeUpdate();

                // Now, update the consommateur table
                String queryConsommateur = "UPDATE consommateur SET type = ? WHERE id = ?";
                try (PreparedStatement preConsommateur = con.prepareStatement(queryConsommateur)) {
                    preConsommateur.setString(1, ((Consommateur) personne).getType().toString());
                    preConsommateur.setInt(2, personne.getId());
                    preConsommateur.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    System.err.println("Error updating consommateur: " + e.getMessage());
                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean deletePersonne(int id) {
        String query = "DELETE FROM personne WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, id);
            int rowsAffected = pre.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //NEW METHOD:  Get ALL Consommateurs Directly
    public List<Consommateur> getAllConsommateurs() {
        List<Consommateur> consommateurList = new ArrayList<>();
        // Modified Query to retrieve all info from the consommateur table
        String query = "SELECT id, nom, prenom, Tel, password, type FROM consommateur";  //CHANGED

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Create Consommateur objects directly from the result set
                Consommateur consommateur = new Consommateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("Tel"),
                        rs.getString("password"),
                        TPA.valueOf(rs.getString("type"))  // Convert string to TPA enum
                );
                consommateurList.add(consommateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consommateurList;
    }


    // Old method - Keep for reference, but not used in ConsommateurController now.
    public List<Personne> getAllPersonnes() {
        List<Personne> personnes = new ArrayList<>();
        String query = "SELECT p.id, p.nom, p.prenom, p.Tel, p.password, c.type " +
                "FROM personne p LEFT JOIN consommateur c ON p.id = c.id";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Personne personne = mapPersonne(rs);
                if (personne != null) {
                    personnes.add(personne);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personnes;
    }

    public Personne getPersonneById(int id) {
        String query = "SELECT p.id, p.nom, p.prenom, p.Tel, p.password, c.type FROM personne p LEFT JOIN consommateur c ON p.id = c.id  WHERE p.id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapPersonne(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Personne mapPersonne(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String tel = rs.getString("Tel");
        String password = rs.getString("password");
        String type = rs.getString("type");
        if (type != null && type.equals("EMPLOYEE")) {
            Employee employee = new Employee(id, nom, prenom, tel, password, rs.getString("post"), rs.getString("matriculeSocial"));
            return employee;
        } else if (type != null) {
            try {
                TPA tpa = TPA.valueOf(type);
                Consommateur consommateur = new Consommateur(id, nom, prenom, tel, password, tpa);
                return consommateur;
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown TPA type: " + type);
                Consommateur consommateur = new Consommateur(id, nom, prenom, tel, password, TPA.ETUDIANT);
                return consommateur;
            }
        } else {
            Personne personne = new Personne(id, nom, prenom, tel, password);
            return personne;
        }
    }
}