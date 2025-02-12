package Services;

import Entites.Repas;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFinance implements IService<Repas> {
    private final Connection con;

    public ServiceFinance() throws SQLException {
        this.con = DataSource.getInstance().getConn();
    }

    @Override
    public void ajouter(Repas repas) throws SQLException {
        String req = "INSERT INTO `repas` (`date`, `nom-repas`, `nbrRepas`, `cout`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setDate(1, new java.sql.Date(repas.getDate().getTime()));
            pre.setString(2, repas.getNom());
            pre.setInt(3, repas.getNbRepas());
            pre.setDouble(4, repas.getCout());
            pre.executeUpdate();
        }
    }

    @Override
    public Boolean supprimer(int id) throws SQLException {
        String req = "DELETE FROM `repas` WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, id);
            return pre.executeUpdate() > 0;
        }
    }

    @Override
    public void update(Repas repas) throws SQLException {
        String req = "UPDATE `repas` SET `date` = ?, `nom-repas` = ?, `nbrRepas` = ?, `cout` = ? WHERE `id` = ?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setDate(1, new java.sql.Date(repas.getDate().getTime()));
            pre.setString(2, repas.getNom());
            pre.setInt(3, repas.getNbRepas());
            pre.setDouble(4, repas.getCout());
            pre.setInt(5, repas.getId());
            pre.executeUpdate();
        }
    }

    @Override
    public Repas getById(int id) throws SQLException {
        String req = "SELECT * FROM `repas` WHERE id = ?";
        try (PreparedStatement pre = con.prepareStatement(req)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return new Repas(
                            rs.getInt("id"),
                            rs.getDate("date"),
                            rs.getString("nom-repas"),
                            rs.getInt("nbrRepas"),
                            rs.getDouble("cout"),
                            rs.getDouble("prix")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public List<Repas> getAll() throws SQLException {
        List<Repas> list = new ArrayList<>();
        String req = "SELECT * FROM repas";
        try (PreparedStatement pre = con.prepareStatement(req);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(new Repas(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("nom-repas"),
                        rs.getInt("nbrRepas"),
                        rs.getDouble("cout"),
                        rs.getDouble("prix")
                ));
            }
        }
        return list;
    }

    @Override
    public List<Repas> getByIdConsomateur(int idConsomateur) {
        return new ArrayList<>();
    }

    public double getTotalCost() throws SQLException {
        String req = "SELECT COALESCE(SUM(nbrRepas * cout), 0) AS total FROM repas";
        try (PreparedStatement pre = con.prepareStatement(req);
             ResultSet rs = pre.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

    public int getTotalMeals() throws SQLException {
        String req = "SELECT COALESCE(SUM(nbrRepas), 0) AS total FROM repas";
        try (PreparedStatement pre = con.prepareStatement(req);
             ResultSet rs = pre.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public double getTotalRevenue() throws SQLException {
        String req = "SELECT COALESCE(SUM(nbrRepas * prix), 0) AS total FROM repas";
        try (PreparedStatement pre = con.prepareStatement(req);
             ResultSet rs = pre.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }
}
