package Controllers.Proposition;

import Entites.Proposition;
import Services.serviceProposition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label ;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class ModifierProposition {
    @FXML
    private TextField txtId;
    @FXML
    private Label label;
    private int currentId;
    @FXML
    private TextField txtDescription;
    private Services.serviceProposition serviceProposition;

    @FXML
    private TextField txtObjet;
    public ModifierProposition() {
        this.serviceProposition = new serviceProposition();
    }

    public void initializeData(Proposition proposition) {
        if (proposition != null) {
            currentId = proposition.getId(); // Stocker l'ID dans la variable
            txtObjet.setText(proposition.getObjet());
            txtDescription.setText(proposition.getDescription());
        } else {
            System.out.println("txtId is null");
        }

    }


    @FXML
    private void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/afficherPropositions.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }
    }


    @FXML
    public void save(javafx.event.ActionEvent actionEvent) throws SQLException {
        String description = txtDescription.getText();
        String objetText = txtObjet.getText();
        if (description.isEmpty() || objetText.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }



        Proposition p1 = new Proposition( description, objetText , currentId);

        serviceProposition.update(p1);

    }
}

