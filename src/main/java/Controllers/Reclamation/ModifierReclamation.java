package Controllers.Reclamation;

import Entites.Proposition;
import Entites.Reclamation;
import Services.ServiceReclamation;
import Services.serviceProposition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class ModifierReclamation {
    @FXML
    private Label label;

    @FXML
    private TextField txtDescription;
    @FXML
    private Label confirmationLabel;

    private int currentId;

    @FXML
    private TextField txtObjet;

    private Services.ServiceReclamation ServiceReclamation;



    public ModifierReclamation() {
        this.ServiceReclamation = new ServiceReclamation();
    }

    @FXML
    public void save(javafx.event.ActionEvent actionEvent) throws SQLException {
        String description = txtDescription.getText();
        String objetText = txtObjet.getText();
        if (description.isEmpty() || objetText.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        Reclamation rec1 = new Reclamation( description, objetText , currentId);

        ServiceReclamation.update(rec1);
        confirmationLabel.setVisible(true);


    }





    public void initializeData(Reclamation reclamation) {
        if (reclamation != null) {
            currentId = reclamation.getId(); // Stocker l'ID dans la variable
            txtObjet.setText(reclamation.getObjet());
            txtDescription.setText(reclamation.getDescription());
        } else {
            System.out.println("txtId is null");
        }

    }

    @FXML
    private void retour(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamations.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }    }
}
