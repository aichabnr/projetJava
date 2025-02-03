package Controllers.Reclamation;


import Entites.Reclamation;
import Services.ServiceReclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class AfficherReclamations {

    @FXML
    private Button btnAddReclamation;

    @FXML
    private TableColumn<Reclamation, ?> colActions;

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
    void initialize() {
        ServiceReclamation reclamationService = new ServiceReclamation();

            List<Reclamation> l1=reclamationService.getAll();
            ObservableList<Reclamation> obse= FXCollections.observableList(l1);
            tableReclamation.setItems(obse);
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colObjet.setCellValueFactory(new PropertyValueFactory<>("objet"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colconsomateur.setCellValueFactory(new PropertyValueFactory<>("idConsomateur"));
    }

    @FXML
    void addReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ajouterReclamation.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Ajouter Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement .");
        }
    }

}
