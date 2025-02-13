package com.esprit.espritrestau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class EspritRestau  extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EspritRestau.class.getResource("ligne_presence.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Authentificate");
        stage.getIcons().add(new Image("file:src/main/resources/images/logo.png")) ;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}