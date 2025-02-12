package Controllers.Repas;

import Entites.Repas;
import Services.serviceRepas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SupprimerRepas {
    @FXML
    private Label label;

    private Stage popupStage;
    private int currentId;

    private Services.serviceRepas serviceRepas;

    public SupprimerRepas() {
        this.serviceRepas = new serviceRepas();
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

    public void initializeData(Repas repas) {
        if (repas != null) {
            currentId = repas.getId(); // Stocker l'ID dans la variable
        } else {
            System.out.println("Repas is null");
        }
    }

    @FXML
    private void handleConfirmButton() {
        serviceRepas.supprimer(currentId); // Appeler le service pour supprimer le repas
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) label.getScene().getWindow();
        currentStage.close();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/afficherRepas.fxml"));
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
