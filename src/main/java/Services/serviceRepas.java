package Services;

import Entites.Reclamation;
import Entites.Repas;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class serviceRepas {

    private Connection con = DataSource.getInstance().getConn();

    public void ajouter(Repas repas) {
        String req = "INSERT INTO `repas` (`id`, `date`, `nom-repas`, `nbrRepas`, `cout`) VALUES (NULL, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(req)) {
            preparedStatement.setString(1, repas.getDate());
            preparedStatement.setString(2, repas.getNom());
            preparedStatement.setInt(3, repas.getNbRepas());
            preparedStatement.setDouble(4, repas.getCout());

            preparedStatement.executeUpdate();
            System.out.println("Repas ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du repas : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Boolean supprimer(int id) {
        String sql = "DELETE FROM repas WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Repas supprimé avec succès.");
                return true;
            } else {
                System.out.println("Repas non trouvé.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void update(Repas repas) {
        String sql = "UPDATE repas SET `date` = ?, `nom-repas` = ?, `nbrRepas` = ?, `cout` = ? WHERE `id` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, repas.getDate());
            stmt.setString(2, repas.getNom());
            stmt.setInt(3, repas.getNbRepas());
            stmt.setDouble(4, repas.getCout());
            stmt.setInt(5, repas.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Repas mis à jour avec succès.");
            } else {
                System.out.println("Aucun repas trouvé avec l'ID : " + repas.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public Repas getById(int id) {
        String sql = "SELECT * FROM repas WHERE `id` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Repas(
                            rs.getInt("id"),
                            rs.getString("date"),
                            rs.getString("nom-repas"),
                            rs.getInt("nbrRepas"),
                            rs.getDouble("cout")
                    );
                } else {
                    System.out.println("Aucun repas trouvé avec l'ID : " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du repas : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Repas> getAll() {
        List<Repas> repasList = new ArrayList<>();
        String sql = "SELECT * FROM repas";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                repasList.add(new Repas(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("nom-repas"),
                        rs.getInt("nbrRepas"),
                        rs.getDouble("cout")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des repas : " + e.getMessage());
            e.printStackTrace();
        }
        return repasList;
    }
    public List<Repas> chercherParNom(String nom) {
        List<Repas> repasList = new ArrayList<>();
        String sql = "SELECT * FROM repas WHERE `nom-repas` LIKE ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%"); // Recherche partielle
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    repasList.add(new Repas(
                            rs.getInt("id"),
                            rs.getString("date"),
                            rs.getString("nom-repas"),
                            rs.getInt("nbrRepas"),
                            rs.getDouble("cout")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des repas : " + e.getMessage());
            e.printStackTrace();
        }
        return repasList;
    }



   
}