package com.esprit.espritrestau;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/espritrestau/view/employee.fxml"));
        VBox root = loader.load();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Employee CRUD");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void mainv1(String[] args) {
        launch(args);
    }
}
