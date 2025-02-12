package Services;


import Entites.Repas;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFinance  {
    private final Connection con;

    public ServiceFinance() throws SQLException {
        this.con = DataSource.getInstance().getConn();
    }







    public List<Repas> getAll() throws SQLException {
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
        String req = "SELECT COALESCE(SUM(nbrRepas * 4 ), 0) AS total FROM repas";
        try (PreparedStatement pre = con.prepareStatement(req);
             ResultSet rs = pre.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }
}