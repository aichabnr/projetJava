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
            String query = "INSERT INTO consommateur (nom, prenom, tel, password, type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pre = con.prepareStatement(query)) {
                pre.setString(1, personne.getNom());
                pre.setString(2, personne.getPrenom());
                pre.setString(3, personne.getTel());
                pre.setString(4, personne.getPassword());

                Consommateur cons = (Consommateur) personne;
                pre.setString(5, cons.getType().name());

                pre.executeUpdate();
                return true;
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
            String query = "UPDATE consommateur SET nom = ?, prenom = ?, tel = ?, password = ?, type = ? WHERE id = ?";
            try (PreparedStatement pre = con.prepareStatement(query)) {
                pre.setString(1, personne.getNom());
                pre.setString(2, personne.getPrenom());
                pre.setString(3, personne.getTel());
                pre.setString(4, personne.getPassword());

                Consommateur cons = (Consommateur) personne;
                pre.setString(5, "ETUDIANT");

                pre.setInt(6, personne.getId());
                pre.executeUpdate();
                return true;
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

    public List<Personne> getAllPersonnes() {
        List<Personne> personnes = new ArrayList<>();
        String query = "SELECT * FROM personne";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                personnes.add(mapPersonne(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personnes;
    }

    public Personne getPersonneById(int id) {
        String query = "SELECT * FROM personne WHERE id = ?";
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
        String tel = rs.getString("tel");
        String password = rs.getString("password");
        String type = rs.getString("type");

        if ("EMPLOYEE".equals(type)) {
            return new Employee(id, nom, prenom, tel, password, rs.getString("post"), rs.getString("matriculeSocial"));
        } else if ("ETUDIANT".equals(type)) {
            return new Consommateur(id, nom, prenom, tel, password, TPA.ETUDIANT);
        } else if ("PERSONNEL".equals(type)) {
            return new Consommateur(id, nom, prenom, tel, password, TPA.PERSONNEL);
        } else {
            return new Personne(id, nom, prenom, tel, password);
        }
    }
}
