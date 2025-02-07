package Controllers.Proposition;

import Entites.Proposition;
import Services.serviceProposition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SupprimerProposition {
    @FXML
   private Label label;
    private int currentId;

    private Stage popupStage;
    private Services.serviceProposition serviceProposition;
    public SupprimerProposition() {
        this.serviceProposition = new serviceProposition();
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    @FXML
    private void handleCloseButton() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
    public void initializeData(Proposition proposition) {
        if (proposition != null) {
            currentId = proposition.getId(); // Stocker l'ID dans la variable
        } else {
            System.out.println("txtId is null");
        }

    }
    @FXML
    private void handleConfirmButton() {
        serviceProposition.supprimer(currentId);
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) label.getScene().getWindow();
        currentStage.close();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/afficherPropositions.fxml"));
                Parent root = loader.load();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }





}
