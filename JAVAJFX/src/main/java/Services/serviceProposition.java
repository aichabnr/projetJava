package Services;

import Entites.Proposition;
import Entites.Reclamation;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class serviceProposition  implements  IService <Proposition>{

    private Connection con = DataSource.getInstance().getConn();
    private Statement stmt;
    @Override
    public void ajouter(Proposition prop) {

        String req = "INSERT INTO `proposition` (`id`, `desc`, `objet`, `idConsomateur`) VALUES (NULL, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(req)) {
            preparedStatement.setString(1, prop.getDescription());
            preparedStatement.setString(2, prop.getObjet());
            preparedStatement.setInt(3, prop.getIdConsomateur());
            preparedStatement.executeUpdate();
            System.out.println("Proposition ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la proposition : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Boolean supprimer(int id) {
        String sql = "DELETE FROM proposition WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Proposition deleted successfully.");
                return true;
            } else {
                System.out.println("Proposition not found.");
                return false
            }
        } catch (SQLException e) {
            System.err.println("Error during deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void update(Proposition proposition) throws SQLException {
        String sql = "UPDATE proposition SET `desc` = ?, `objet` = ?, `idConsomateur` = ? WHERE `id` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, proposition.getDescription());
            stmt.setString(2, proposition.getObjet());
            stmt.setInt(3, proposition.getIdConsomateur());
            stmt.setInt(4, proposition.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("proposition updated successfully.");
            } else {
                System.out.println("proposition not found with ID: " + proposition.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error during update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Proposition getById(int id) throws SQLException {
        String sql = "SELECT * FROM proposition WHERE `id` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Proposition prop = new Proposition();
                    prop.setId(rs.getInt("id"));
                    prop.setDescription(rs.getString("desc"));
                    prop.setObjet(rs.getString("objet"));
                    prop.setIdConsomateur(rs.getInt("idConsomateur"));
                    return prop;
                } else {
                    System.out.println("No proposition found with ID: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving reclamation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Proposition> getAll() {
        List<Proposition> propositions = new ArrayList<>();
        String sql = "SELECT  *  FROM proposition";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Proposition proposition = new Proposition();
                proposition.setId(rs.getInt("id"));
                proposition.setDescription(rs.getString("desc"));
                proposition.setObjet(rs.getString("objet"));
                proposition.setIdConsomateur(rs.getInt("idConsomateur"));
                propositions.add(proposition);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving propositions: " + e.getMessage());
            e.printStackTrace();
        }
        return propositions;
    }


    @Override
    public List<Proposition> getByIdConsomateur(int idConsomateur) {
        List<Proposition> propositions = new ArrayList<>();
        String sql = "SELECT * FROM proposition WHERE `idConsomateur` = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idConsomateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Proposition proposition = new Proposition();
                    proposition.setId(rs.getInt("id"));
                    proposition.setDescription(rs.getString("desc"));
                    proposition.setObjet(rs.getString("objet"));
                    proposition.setIdConsomateur(rs.getInt("idConsomateur"));
                    propositions.add(proposition);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving proposition for idConsomateur: " + idConsomateur);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return propositions;
    }



}
