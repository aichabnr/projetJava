package Services;

import Entites.Reclamation;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation implements IService <Reclamation>{
    private Connection con = DataSource.getInstance().getConn();
    private Statement stmt;
    @Override
    public void ajouter(Reclamation rec) throws SQLException {
        String req = "INSERT INTO `reclamation` (`id`, `date`, `description`, `objet`, `idConsomateur`) VALUES (NULL, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = con.prepareStatement(req);
        java.util.Date utilDate = rec.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        preparedStatement.setDate(1, sqlDate);  // Utilise setDate pour la colonne 'date'
        preparedStatement.setString(2, rec.getDescription());  // Remplir description
        preparedStatement.setString(3, rec.getObjet());  // Remplir objet
        preparedStatement.setInt(4, rec.getIdConsomateur());  // Remplir idConsomateur

        // Exécuter la requête
        preparedStatement.executeUpdate();
    }


    @Override
    public Boolean supprimer(int id) {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("reclamation deleted successfully.");
                return true;
            } else {
                System.out.println("reclamation not found.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void update(Reclamation reclamation) {
        String sql = "UPDATE reclamation SET `description` = ?, `objet` = ? WHERE `id` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, reclamation.getDescription());
            stmt.setString(2, reclamation.getObjet());
            stmt.setInt(3, reclamation.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reclamation updated successfully.");
            } else {
                System.out.println("Reclamation not found with ID: " + reclamation.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error during update: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public Reclamation getById(int id) {
        String sql = "SELECT * FROM reclamation WHERE `id` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(rs.getInt("id"));
                    java.sql.Date sqlDate = rs.getDate("date");  // Utilise la colonne "date"
                    reclamation.setDate(new java.util.Date(sqlDate.getTime())); // Convertir en java.util.Date         reclamation.setDescription(rs.getString("desc"));
                    reclamation.setObjet(rs.getString("objet"));
                    reclamation.setIdConsomateur(rs.getInt("idConsomateur"));
                    return reclamation;
                } else {
                    System.out.println("No reclamation found with ID: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving reclamation: " + e.getMessage());
            e.printStackTrace();
            return null; // Retourne null en cas d'erreur
        }
    }


    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation"; // Utilisation d'un SELECT simple

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();

                reclamation.setId(rs.getInt("id"));

                java.sql.Date sqlDate = rs.getDate("date");  // Récupérer la colonne 'date'
                if (sqlDate != null) {
                    reclamation.setDate(new java.util.Date(sqlDate.getTime())); // Conversion en java.util.Date
                }

                reclamation.setDescription(rs.getString("description"));
                reclamation.setObjet(rs.getString("objet"));
                reclamation.setIdConsomateur(rs.getInt("idConsomateur"));

                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving reclamations: " + e.getMessage());
            e.printStackTrace();
        }

        // Retour de la liste de réclamations
        return reclamations;
    }

    @Override
    public List<Reclamation> getByIdConsomateur(int idConsomateur) {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation WHERE `idConsomateur` = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idConsomateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(rs.getInt("id"));
                    java.sql.Date sqlDate = rs.getDate("date");  // Utilise la colonne "date"
                    reclamation.setDate(new java.util.Date(sqlDate.getTime())); // Convertir en java.util.Date                    reclamation.setDescription(rs.getString("desc"));
                    reclamation.setObjet(rs.getString("objet"));
                    reclamation.setIdConsomateur(rs.getInt("idConsomateur"));
                    reclamations.add(reclamation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving reclamations for idConsomateur: " + idConsomateur);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return reclamations;
    }
    public List<Reclamation> chercher(String searchTerm) {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation WHERE `objet` LIKE ? OR `description` LIKE ?";  // Recherche dans objet et description

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");  // Recherche partielle dans objet
            stmt.setString(2, "%" + searchTerm + "%");  // Recherche partielle dans description

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(rs.getInt("id"));
                    java.sql.Date sqlDate = rs.getDate("date");
                    if (sqlDate != null) {
                        reclamation.setDate(new java.util.Date(sqlDate.getTime()));
                    }
                    reclamation.setDescription(rs.getString("description"));
                    reclamation.setObjet(rs.getString("objet"));
                    reclamation.setIdConsomateur(rs.getInt("idConsomateur"));
                    reclamations.add(reclamation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while searching reclamations: " + e.getMessage());
            e.printStackTrace();
        }

        return reclamations;
    }

}
