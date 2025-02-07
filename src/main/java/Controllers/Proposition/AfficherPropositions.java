package Controllers.Proposition;


import Entites.Proposition;
import Services.serviceProposition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.InputStream;
import java.util.List;



public class AfficherPropositions {

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

            // Configurer la cellFactory pour la colonne "Modifier"
            colUpdate.setCellFactory(param -> {
                TableCell<Proposition, Void> cell = new TableCell<Proposition, Void>() {
                    private final Button btnEdit = new Button();

                    {
                        // Charger l'icône "Modifier"
                        InputStream editIconStream = getClass().getResourceAsStream("/images/edit.png");
                        if (editIconStream == null) {
                            System.err.println("L'icône 'edit.png' n'a pas été trouvée !");
                        } else {
                            ImageView editIcon = new ImageView(new Image(editIconStream));
                            editIcon.setFitHeight(16);
                            editIcon.setFitWidth(16);
                            btnEdit.setGraphic(editIcon);
                            btnEdit.setStyle("-fx-background-color:  #850f36; -fx-border-width: 0; -fx-padding: 5;");

                        }

                        // Définir l'action du bouton "Modifier"
                        btnEdit.setOnAction(event -> {
                            Proposition proposition = getTableView().getItems().get(getIndex());
                            handleEditAction(proposition, event); // Passer l'événement en paramètre
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnEdit);
                        }
                    }
                };
                return cell;
            });

            // Configurer la cellFactory pour la colonne "Supprimer"
            colDelet.setCellFactory(param -> {
                TableCell<Proposition, Void> cell = new TableCell<Proposition, Void>() {
                    private final Button btnDelete = new Button();

                    {
                        // Charger l'icône "Supprimer"
                        InputStream deleteIconStream = getClass().getResourceAsStream("/images/delete.png");
                        if (deleteIconStream == null) {
                            System.err.println("L'icône 'delete.png' n'a pas été trouvée !");
                        } else {
                            ImageView deleteIcon = new ImageView(new Image(deleteIconStream));
                            deleteIcon.setFitHeight(16);
                            deleteIcon.setFitWidth(16);
                            btnDelete.setGraphic(deleteIcon);
                            btnDelete.setStyle("-fx-background-color:  #850f36; -fx-border-width: 0; -fx-padding: 5;");

                        }

                        // Définir l'action du bouton "Supprimer"
                        btnDelete.setOnAction(event -> {
                            Proposition proposition = getTableView().getItems().get(getIndex());
                            handleDeleteAction(proposition);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                        }
                    }
                };
                return cell;
            });

        } catch (Exception e) {
            // Gérer les erreurs (par exemple, afficher un message à l'utilisateur)
            System.err.println("Erreur lors du chargement des propositions : " + e.getMessage());
            e.printStackTrace();
        }
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


    private void handleDeleteAction(Proposition proposition) {
        System.out.println("supprimer  la proposition : " + proposition.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/supprimerProposition.fxml"));
            Parent root = loader.load();
            // Passer la proposition au controller de la page modifierProposition
            SupprimerProposition controller = loader.getController();
            controller.initializeData(proposition);

            Scene scene = new Scene(root);
            Stage popupStage = new Stage();

            popupStage.setTitle("Popup");
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquer l'interaction avec la fenêtre principale
            popupStage.setScene(scene);

            SupprimerProposition  popupController = loader.getController();
            popupController.setPopupStage(popupStage);

            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la popup.");
        }
    }


    private void handleEditAction(Proposition proposition, ActionEvent event) {
        System.out.println("Modifier la proposition : " + proposition.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Proposition/modifierProposition.fxml"));
            Parent root = loader.load();

            // Passer la proposition au controller de la page modifierProposition
            ModifierProposition controller = loader.getController();
            controller.initializeData(proposition);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modifier Proposition");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }
    }

}



