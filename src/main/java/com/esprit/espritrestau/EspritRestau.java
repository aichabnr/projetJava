package com.esprit.espritrestau;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.services.AbonnementService;
import com.esprit.espritrestau.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class EspritRestau extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EspritRestau.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {



        AbonnementService service = new AbonnementService();

        // Test Add
        Abonnement newAbonnement = new Abonnement(142, new java.util.Date(), new java.util.Date(), 1200.0, 101);
        service.addAbonnement(newAbonnement);


        List<Abonnement> allAbonnements = service.getAllAbonnements();
        System.out.println("All Abonnements: " + allAbonnements.toString());

    }
}