package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private Connection conn;

    private static String url = "jdbc:mysql://localhost:3306/restoesprit";
    private  String user = "root";
    private  String pass = "";
    private static DataSource data;

     public DataSource() {

        try {
            conn= DriverManager.getConnection(url,user,pass);
            System.out.println("connexion établie");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Connection getConn() {
        return conn;
    }

    public static DataSource getInstance(){
        if(data == null){

            data = new DataSource();
        }
        return data;
    }
}
