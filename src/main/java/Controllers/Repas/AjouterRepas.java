package Controllers.Repas;

import Entites.Proposition;
import Entites.Repas;
import Services.serviceProposition;
import Services.serviceRepas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
public class AjouterRepas {

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCout;

    private Services.serviceRepas serviceRepas;


    public AjouterRepas() {
        this.serviceRepas = new serviceRepas();
    }



    @FXML
    void save(ActionEvent event) {
        // Récupérer les valeurs des champs
        String nom = txtNom.getText();
        String date = txtDate.getValue() != null ? txtDate.getValue().toString() : "";
        int nbRepas = txtNombre.getText().isEmpty() ? 0 : Integer.parseInt(txtNombre.getText());
        double cout = txtCout.getText().isEmpty() ? 0.0 : Double.parseDouble(txtCout.getText());

        // Vérification des champs
        serviceRepas service = new serviceRepas();
        Repas rep1 = new Repas(1,date,nom, nbRepas, cout);
        if (nom.isEmpty() || date.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }
        serviceRepas.ajouter(rep1); // Appel du service
    }

    @FXML
    private void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/AfficherRepas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Repas");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page Repas.");
        }
    }



}
