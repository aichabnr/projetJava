package Controllers.Repas;

import Entites.Repas;
import Services.serviceRepas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ModifierRepas {


    @FXML
    private TextField txtCout;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtNombre;
    private int currentId;

    private serviceRepas serviceRepas;

    public ModifierRepas() {
        this.serviceRepas = new serviceRepas();
    }
    // Initialiser les données de Repas
    public void initializeData(Repas repas) {
        if (repas != null) {
            currentId = repas.getId(); // Stocker l'ID dans la variable
            txtNom.setText(repas.getNom());
            txtNombre.setText(String.valueOf(repas.getNbRepas()));
            txtCout.setText(String.valueOf(repas.getCout()));
        } else {
            System.out.println("Le repas est nul");
        }
    }

    @FXML
    public void save(javafx.event.ActionEvent actionEvent) throws SQLException {

        // Récupérer les valeurs des champs
        String nom = txtNom.getText();
        int nbRepas = txtNombre.getText().isEmpty() ? 0 : Integer.parseInt(txtNombre.getText());
        double cout = txtCout.getText().isEmpty() ? 0.0 : Double.parseDouble(txtCout.getText());
        String date = java.time.LocalDate.now().toString();

        // Vérification des champs


        Repas repas = new Repas(currentId , nom, date ,  nbRepas, cout);

        serviceRepas.update(repas);


    }



    @FXML
    private void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/afficherRepas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Liste des Repas");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }
    }
}
