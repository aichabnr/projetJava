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
                pre.setString(5, ((Employee) personne).getPost());
                pre.setString(6, ((Employee) personne).getMatriculeSocial());
                pre.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else if (personne instanceof Consommateur) {

            String queryPersonne = "INSERT INTO personne (nom, prenom, Tel, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement prePersonne = con.prepareStatement(queryPersonne, Statement.RETURN_GENERATED_KEYS)) {
                prePersonne.setString(1, personne.getNom());
                prePersonne.setString(2, personne.getPrenom());
                prePersonne.setString(3, personne.getTel());
                prePersonne.setString(4, personne.getPassword());
                prePersonne.executeUpdate();


                ResultSet generatedKeys = prePersonne.getGeneratedKeys();
                int personneId;
                if (generatedKeys.next()) {
                    personneId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert personne, no ID obtained.");
                }

                // Now, insert into the consommateur table
                String queryConsommateur = "INSERT INTO consommateur (id, type,nom, prenom, Tel, password) VALUES (?, ?,?,?,?,?)";
                try (PreparedStatement preConsommateur = con.prepareStatement(queryConsommateur)) {
                    preConsommateur.setInt(1, personneId); // Use the generated ID
                    preConsommateur.setString(2, ((Consommateur) personne).getType().toString());
                    preConsommateur.setString(3, personne.getNom());
                    preConsommateur.setString(4, personne.getPrenom());
                    preConsommateur.setString(5, personne.getTel());
                    preConsommateur.setString(6, personne.getPassword());
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
            String query = "UPDATE employee SET nom = ?, prenom = ?, tel = ?, password = ?, post = ?, matriculeSocial = ? WHERE id = ?";
            try (PreparedStatement pre = con.prepareStatement(query)) {
                pre.setString(1, personne.getNom());
                pre.setString(2, personne.getPrenom());
                pre.setString(3, personne.getTel());
                pre.setString(4, personne.getPassword());
                pre.setString(5, ((Employee) personne).getPost());
                pre.setString(6, ((Employee) personne).getMatriculeSocial());
                pre.setInt(7, personne.getId());
                pre.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else if (personne instanceof Consommateur) {

            String queryPersonne = "UPDATE consommateur SET nom = ?, prenom = ?, Tel = ?, password = ? WHERE id = ?";
            try (PreparedStatement prePersonne = con.prepareStatement(queryPersonne)) {
                prePersonne.setString(1, personne.getNom());
                prePersonne.setString(2, personne.getPrenom());
                prePersonne.setString(3, personne.getTel());
                prePersonne.setString(4, personne.getPassword());
                prePersonne.setInt(5, personne.getId());
                System.out.println(prePersonne);
                prePersonne.executeUpdate();


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


    public boolean deletePersonne(Personne personne) {
        String query = null;
        try (PreparedStatement pre = (personne instanceof Employee) ? con.prepareStatement("DELETE FROM employee WHERE id = ?") : con.prepareStatement("DELETE FROM consommateur WHERE id = ?")) {

            pre.setInt(1, personne.getId());
            int rowsAffected = pre.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Consommateur> getAllConsommateurs() {
        List<Consommateur> consommateurList = new ArrayList<>();
        String query = "SELECT id, nom, prenom, Tel, password, type FROM consommateur";  //CHANGED

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Consommateur consommateur = new Consommateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("Tel"),
                        rs.getString("password"),
                        TPA.valueOf(rs.getString("type"))
                );
                consommateurList.add(consommateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consommateurList;
    }


    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT id, nom, prenom, tel, password, post, matriculeSocial FROM employee";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("tel"),
                        rs.getString("password"),
                        rs.getString("post"),
                        rs.getString("matriculeSocial")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
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