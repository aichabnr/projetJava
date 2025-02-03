package Controllers.Proposition;

import Entites.Proposition;
import Services.serviceProposition;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent; // Utiliser ActionEvent de JavaFX


public class AjouterProposition {

    @FXML
    private Label label;

    @FXML
    private TextField des;

    @FXML
    private TextField objet;
    private Services.serviceProposition serviceProposition;


    public AjouterProposition() {
        this.serviceProposition = new serviceProposition();
    }

    @FXML
    void save(ActionEvent event) {
        String description = des.getText();
        String objetText = objet.getText();
        Proposition p1 = new Proposition(description,objetText,1);
        if (description.isEmpty() || objetText.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }
        serviceProposition.ajouter(p1); // Appel du service
    }

    }

