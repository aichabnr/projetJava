package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.*;
import com.esprit.espritrestau.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne> {
    private Connection con = DataSource.getConnection();
    private Statement st;

    public ServicePersonne() throws SQLException {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(Personne personne) throws SQLException {
        String req = "INSERT INTO personne (nom, prenom, tel, password, type, post, matriculeSocial) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, personne.getNom());
            pre.setString(2, personne.getPrenom());
            pre.setInt(3, personne.getTel());
            pre.setString(4, personne.getPassword());

            if (personne instanceof Employee) {
                Employee emp = (Employee) personne;
                pre.setString(5, "EMPLOYEE");
                pre.setString(6, emp.getPost());
                pre.setString(7, emp.getMatriculeSocial());
            } else if (personne instanceof Consommateur) {
                Consommateur cons = (Consommateur) personne;
                pre.setString(5, cons.getType().name());
                pre.setNull(6, Types.VARCHAR);
                pre.setNull(7, Types.VARCHAR);
            } else {
                pre.setNull(5, Types.VARCHAR);
                pre.setNull(6, Types.VARCHAR);
                pre.setNull(7, Types.VARCHAR);
            }
            pre.executeUpdate();
        }
    }

    @Override
    public Boolean supprimer(int id) throws SQLException {
        return null;
    }

    @Override
    public void supprimer(Personne personne) throws SQLException {
        String req = "DELETE FROM personne WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, personne.getId());
            pre.executeUpdate();
        }
    }

    @Override
    public void update(Personne personne) throws SQLException {
        String req = "UPDATE personne SET nom = ?, prenom = ?, tel = ?, password = ?, type = ?, post = ?, matriculeSocial = ? WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setString(1, personne.getNom());
            pre.setString(2, personne.getPrenom());
            pre.setInt(3, personne.getTel());
            pre.setString(4, personne.getPassword());

            if (personne instanceof Employee) {
                Employee emp = (Employee) personne;
                pre.setString(5, "EMPLOYEE");
                pre.setString(6, emp.getPost());
                pre.setString(7, emp.getMatriculeSocial());
            } else if (personne instanceof Consommateur) {
                Consommateur cons = (Consommateur) personne;
                pre.setString(5, cons.getType().name());
                pre.setNull(6, Types.VARCHAR);
                pre.setNull(7, Types.VARCHAR);
            } else {
                pre.setNull(5, Types.VARCHAR);
                pre.setNull(6, Types.VARCHAR);
                pre.setNull(7, Types.VARCHAR);
            }
            pre.setInt(8, personne.getId());
            pre.executeUpdate();
        }
    }

    @Override
    public List<Personne> getAll() throws SQLException {
        List<Personne> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM personne");
        while (rs.next()) {
            list.add(mapPersonne(rs));
        }
        return list;
    }

    @Override
    public List<Personne> getByIdConsomateur(int idConsomateur) {
        return List.of();
    }

    @Override
    public Personne getById(int id) throws SQLException {
        String query = "SELECT * FROM personne WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapPersonne(rs);
                }
            }
        }
        return null;
    }

    private Personne mapPersonne(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        int tel = rs.getInt("tel");
        String password = rs.getString("password");
        String type = rs.getString("type");

        if ("EMPLOYEE".equals(type)) {
            return new Employee(id, nom, prenom, tel, password, rs.getString("post"), rs.getString("matriculeSocial"));
        } else if ("ETUDIANT".equals(type)) {
            return new Consommateur(id, nom, prenom, tel, password, TPA.ETUD);
        } else if ("PERSONNEL".equals(type)) {
            return new Consommateur(id, nom, prenom, tel, password, TPA.PERS);
        } else {
            return new Personne(id, nom, prenom, tel, password);
        }
    }
}
