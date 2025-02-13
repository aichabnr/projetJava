package test;

import Utils.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Test extends Application {
    public static void main(String[] args) {

        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamationsForAdmin.fxml"));

          //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamations.fxml"));

            // Charger sans forcer le type de conteneur
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Ajouter Proposition");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}