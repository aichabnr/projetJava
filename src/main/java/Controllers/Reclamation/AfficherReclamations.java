package Controllers.Reclamation;


import Controllers.Proposition.ModifierProposition;
import Controllers.Proposition.SupprimerProposition;
import Entites.Proposition;
import Entites.Reclamation;
import Services.ServiceReclamation;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class AfficherReclamations {

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
    void initialize() {
        try {
            ServiceReclamation reclamationService = new ServiceReclamation();

            // Charger toutes les réclamations
            List<Reclamation> l1 = reclamationService.getAll();
            ObservableList<Reclamation> obse = FXCollections.observableList(l1);
            tableReclamation.setItems(obse);

            // Définir les valeurs des colonnes
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colObjet.setCellValueFactory(new PropertyValueFactory<>("objet"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colconsomateur.setCellValueFactory(new PropertyValueFactory<>("idConsomateur"));

            // Configurer la cellFactory pour la colonne "Modifier"
            colUpdate.setCellFactory(param -> {
                TableCell<Reclamation, Void> cell = new TableCell<Reclamation, Void>() {
                    private final Button btnEdit = new Button();

                    {
                        // Charger l'icône "Modifier"
                        InputStream editIconStream = getClass().getResourceAsStream("/images/edit.png");
                        if (editIconStream == null) {
                            System.err.println("L'icône 'refresh.png' n'a pas été trouvée !");
                        } else {
                            ImageView editIcon = new ImageView(new Image(editIconStream));
                            editIcon.setFitHeight(16);
                            editIcon.setFitWidth(16);
                            btnEdit.setGraphic(editIcon);
                            btnEdit.setStyle("-fx-background-color:  #850f36; -fx-border-width: 0; -fx-padding: 5;");

                        }

                        // Définir l'action du bouton "Modifier"
                        btnEdit.setOnAction(event -> {
                            Reclamation reclamation = getTableView().getItems().get(getIndex());
                            handleEditAction(reclamation, event); // Passer l'événement en paramètre
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
                TableCell<Reclamation, Void> cell = new TableCell<Reclamation, Void>() {
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
                            Reclamation reclamation = getTableView().getItems().get(getIndex());
                            handleDeleteAction(reclamation);
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
            System.err.println("Erreur lors du chargement des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
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

    private void handleDeleteAction(Reclamation reclamation) {
        System.out.println("supprimer  la reclamation : " + reclamation.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/supprimerReclamation.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage popupStage = new Stage();
            // Passer la proposition au controller de la page modifierProposition
            SupprimerReclamation  controller = loader.getController();
            controller.initializeData(reclamation);

            popupStage.setTitle("Popup");
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquer l'interaction avec la fenêtre principale
            popupStage.setScene(scene);

            SupprimerReclamation popupController = loader.getController();
            popupController.setPopupStage(popupStage);

            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la popup.");
        }
    }
    private void handleEditAction(Reclamation reclamation, ActionEvent event) {
        System.out.println("Modifier la proposition : " + reclamation.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/modifierReclamation.fxml"));
            Parent root = loader.load();

            // Passer la proposition au controller de la page modifierProposition
            ModifierReclamation controller = loader.getController();
            controller.initializeData(reclamation);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modifier reclamation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }
    }

}
