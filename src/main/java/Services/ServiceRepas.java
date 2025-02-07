package Services;

import Entites.Repas;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepas implements IService<Repas> {

    private Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public ServiceRepas() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(Repas repas) throws SQLException {
        String req = "INSERT INTO `repas` (`date`, `nom-repas`, `nbrRepas`, `cout`) " +
                "VALUES ('" + new java.sql.Date(repas.getDate().getTime()) + "', '" +
                repas.getNom() + "', '" + repas.getNbRepas() + "', '" + repas.getCout() + "');";
        st.executeUpdate(req);
    }

    @Override
    public Boolean supprimer(int id) throws SQLException {
        PreparedStatement pre = con.prepareStatement("DELETE FROM `repas` WHERE id = ?");
        pre.setInt(1, id);
        return pre.executeUpdate() > 0;
    }

    @Override
    public void update(Repas repas) throws SQLException {
        PreparedStatement pre = con.prepareStatement(
                "UPDATE `repas` SET `date` = ?, `nom-repas` = ?, `nbrRepas` = ?, `cout` = ? WHERE `id` = ?"
        );
        pre.setDate(1, new java.sql.Date(repas.getDate().getTime()));
        pre.setString(2, repas.getNom());
        pre.setInt(3, repas.getNbRepas());
        pre.setDouble(4, repas.getCout());
        pre.setInt(5, repas.getId());
        pre.executeUpdate();
    }

    @Override
    public Repas getById(int id) throws SQLException {
        PreparedStatement pre = con.prepareStatement("SELECT * FROM repas WHERE id = ?");
        pre.setInt(1, id);
        ResultSet rs = pre.executeQuery();

        if (rs.next()) {
            return new Repas(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    rs.getString("nom-repas"),
                    rs.getInt("nbrRepas"),
                    rs.getDouble("cout")
            );
        }
        return null;
    }

    @Override
    public List<Repas> getAll() throws SQLException {
        List<Repas> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM repas");

        while (rs.next()) {
            Repas repas = new Repas(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    rs.getString("nom-repas"),
                    rs.getInt("nbrRepas"),
                    rs.getDouble("cout")
            );
            list.add(repas);
        }
        return list;
    }

    @Override
    public List<Repas> getByIdConsomateur(int idConsomateur) {
        return List.of();
    }


    public double getTotalCost() throws SQLException {
        ResultSet rs = st.executeQuery("SELECT SUM(cout) as total FROM repas");
        if (rs.next()) {
            return rs.getDouble("total");
        }
        return 0.0;
    }

    public int getTotalMeals() throws SQLException {
        ResultSet rs = st.executeQuery("SELECT SUM(nbrRepas) as total FROM repas");
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }
}
