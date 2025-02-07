package Controllers.Reclamation;

import Entites.Reclamation;
import Services.ServiceReclamation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SupprimerReclamation {
    @FXML
    private Label label;

    private Stage popupStage;
    private int currentId;

    private Services.ServiceReclamation ServiceReclamation;

    public SupprimerReclamation() {
        this.ServiceReclamation = new ServiceReclamation();
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


    public void initializeData(Reclamation reclamation) {
        if (reclamation != null) {
            currentId = reclamation.getId(); // Stocker l'ID dans la variable
        } else {
            System.out.println("txtId is null");
        }

    }
    @FXML
    private void handleConfirmButton() {
        ServiceReclamation.supprimer(currentId);
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) label.getScene().getWindow();
        currentStage.close();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamations.fxml"));
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
