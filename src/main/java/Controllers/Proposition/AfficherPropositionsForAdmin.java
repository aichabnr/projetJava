package Controllers.Proposition;

import Entites.Proposition;
import Services.serviceProposition;
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

public class AfficherPropositionsForAdmin {
    @FXML
    private Button btnAddProposition;

    @FXML
    private TableColumn<Proposition, Proposition> colActions;

    @FXML
    private TableColumn<Proposition, ?> colDescription;

    @FXML
    private TableColumn<Proposition, ?> colId;

    @FXML
    private TableColumn<Proposition, ?> colObjet;

    @FXML
    private TableView<Proposition> tableProposition;

    @FXML
    private TitledPane titledPane;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableColumn<?, ?> colConsomateur;

    @FXML
    private TableColumn<Proposition, Void>  colDelet;


    @FXML
    private TableColumn<Proposition, Void> colUpdate;

    @FXML
    private Button chercher;





    @FXML
    void initialize() {
        // Initialisation du service
        serviceProposition serviceProposition = new serviceProposition();

        try {
            // Récupérer toutes les propositions
            List<Proposition> propositions = serviceProposition.getAll();

            // Convertir la liste en ObservableList
            ObservableList<Proposition> observableList = FXCollections.observableArrayList(propositions);

            // Lier les données à la TableView
            tableProposition.setItems(observableList);

            // Configurer les CellValueFactory pour chaque colonne
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colObjet.setCellValueFactory(new PropertyValueFactory<>("objet"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colConsomateur.setCellValueFactory(new PropertyValueFactory<>("idConsomateur"));


        } catch (Exception e) {
            // Gérer les erreurs (par exemple, afficher un message à l'utilisateur)
            System.err.println("Erreur lors du chargement des propositions : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void chercher(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();
        serviceProposition serviceProposition = new serviceProposition();

        try {
            List<Proposition> propositions;
            if (searchTerm.isEmpty()) {
                propositions = serviceProposition.getAll();
            } else {
                propositions = serviceProposition.chercher(searchTerm);
            }

            ObservableList<Proposition> observableList = FXCollections.observableArrayList(propositions);

            tableProposition.setItems(observableList);

        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des propositions : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void openAbonnement(ActionEvent event) {

    }

    @FXML
    void openAnalyse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Finance/FinanceManagement.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Finance");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la finance.");
        }
    }

    @FXML
    void openPersonnes(ActionEvent event) {

    }

    @FXML
    void openProposition(ActionEvent event) {
        try {
            // Charger le fichier FXML de la proposition
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/afficherPropositionsForAdmin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la proposition.");
        }
    }

    @FXML
    void openReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/afficherReclamationsForAdmin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Reclamation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la Reclamation.");
        }
    }

    @FXML
    void openRepas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/AfficherRepas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Reclamation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la repas.");
        }
    }
}
