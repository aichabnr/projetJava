package test;

import Utils.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
          /////////////Admin
 FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
////Consomateur
////////////FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuConsumer.fxml"));

            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Ajouter Proposition");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}