package com.esprit.espritrestau;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.entities.TPA;
import com.esprit.espritrestau.services.AbonnementService;
import com.esprit.espritrestau.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class EspritRestau  {

    //extends Application

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(EspritRestau.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }

    public static void main(String[] args) {


       AbonnementService service = new AbonnementService();
//        Consommateur m = new Consommateur(2,"Jlassi","Badis",24571421,"12345 ",TPA.ETUDIANT) ;
//
//        Abonnement newAbonnement = new Abonnement(152, new java.util.Date(), new java.util.Date(), 1200.0, m.getId());
//        service.addAbonnement(newAbonnement);
//
//        List<Abonnement> allAbonnements = service.getAllAbonnements();
//        System.out.println("All Abonnements: " + allAbonnements.toString());

        service.consommerRepas(1,1);


    }
}