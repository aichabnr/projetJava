package Controllers.Reclamation;

import Entites.Reclamation;
import Services.ServiceReclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;

public class AfficherReclamationsForAdmin {

    @FXML
    private Button btnAddReclamation;

    @FXML
    private TableColumn<Reclamation, Void> colDelet;

    @FXML
    private TableColumn<Reclamation, Void> colUpdate;
    @FXML
    private TableColumn<Reclamation, ?> colDate;

    @FXML
    private TableColumn<Reclamation, ?> colDescription;

    @FXML
    private TableColumn<Reclamation, ?> colId;

    @FXML
    private TableColumn<Reclamation, ?> colObjet;

    @FXML
    private TableColumn<Reclamation, ?> colconsomateur;

    @FXML
    private TableView<Reclamation> tableReclamation;

    @FXML
    private TitledPane titledPane;

    @FXML
    private TextField txtSearch;
    @FXML
    private Button Chercher;




    @FXML
    void initialize() {
        try {
            ServiceReclamation reclamationService = new ServiceReclamation();

            List<Reclamation> l1 = reclamationService.getAll();

            ObservableList<Reclamation> obse = FXCollections.observableList(l1);
            tableReclamation.setItems(obse);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colObjet.setCellValueFactory(new PropertyValueFactory<>("objet"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colconsomateur.setCellValueFactory(new PropertyValueFactory<>("idConsomateur"));





        } catch (Exception e) {
            // Gérer les erreurs (par exemple, afficher un message à l'utilisateur)
            System.err.println("Erreur lors du chargement des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    void chercher(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();

        ServiceReclamation serviceReclamation = new ServiceReclamation();

        try {
            List<Reclamation> reclamations;
            if (searchTerm.isEmpty()) {
                reclamations = serviceReclamation.getAll();
            } else {
                reclamations = serviceReclamation.chercher(searchTerm);
            }

            ObservableList<Reclamation> observableList = FXCollections.observableArrayList(reclamations);

            tableReclamation.setItems(observableList);

        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }


}
