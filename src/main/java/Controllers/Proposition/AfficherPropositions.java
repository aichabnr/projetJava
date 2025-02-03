package Controllers.Proposition;


import Entites.Proposition;
import Services.serviceProposition;
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



public class AfficherPropositions {

    @FXML
    private Button btnAddProposition;

    @FXML
    private TableColumn<Proposition, ?> colActions;

    @FXML
    private TableColumn<Proposition, ?> colConsomateur;

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
    void initialize() {
        serviceProposition serviceProposition  = new serviceProposition ();

        List<Proposition> l1=serviceProposition.getAll();
        ObservableList<Proposition> obse= FXCollections.observableList(l1);
        tableProposition.setItems(obse);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colObjet.setCellValueFactory(new PropertyValueFactory<>("objet"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colConsomateur.setCellValueFactory(new PropertyValueFactory<>("idConsomateur"));



    }

    @FXML
    void addProposition(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/ajouterProposition.fxml"));
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
