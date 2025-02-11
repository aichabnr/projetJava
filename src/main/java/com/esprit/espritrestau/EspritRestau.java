package com.esprit.espritrestau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class EspritRestau  extends Application {


    @Override
    public void start(Stage stage) throws IOException {
     FXMLLoader fxmlLoader = new FXMLLoader(EspritRestau.class.getResource("consommateur.fxml"));
  //  FXMLLoader fxmlLoader = new FXMLLoader(EspritRestau.class.getResource("employee.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("gestion consommateur");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.getIcons().add(new Image("file:src/main/resources/images/logo.png")) ;
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}

