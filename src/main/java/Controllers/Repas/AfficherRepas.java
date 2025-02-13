// package Controllers.Repas;





// import javafx.fxml.FXML;
// import javafx.scene.control.Button;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.TextField;
// import javafx.scene.control.TitledPane;

//     public class AfficherRepas {

//         @FXML
//         private Button btnAddStudent;

//         @FXML
//         private TableColumn<?, ?> colActions;

//         @FXML
//         private TableColumn<?, ?> colActions1;

//         @FXML
//         private TableColumn<?, ?> colActions11;

//         @FXML
//         private TableColumn<?, ?> colDescription;

//         @FXML
//         private TableColumn<?, ?> colId;

//         @FXML
//         private TableColumn<?, ?> colObjet;

//         @FXML
//         private TableView<?> tableStudent;

//         @FXML
//         private TitledPane titledPane;

//         @FXML
//         private TextField txtSearch;

//     }


package Controllers.Repas;

import Controllers.Reclamation.SupprimerReclamation;
import Entites.Proposition;
import Entites.Reclamation;
import Entites.Repas;
import Services.serviceProposition;
import Services.serviceRepas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AfficherRepas {
    @FXML
    private Button btnAddStudent;

    @FXML
    private TableColumn<Repas, Void> colActions11;

    @FXML
    private TableColumn<Repas, Void> colCout;

    @FXML
    private TableColumn<Repas, Void> colDate;

    @FXML
    private TableColumn<Repas, Void> colId;

    @FXML
    private TableColumn<Repas, Void> colNbr;

    @FXML
    private TableColumn<Repas, Void> colNom;

    @FXML
    private TableView<Repas> tableStudent;

    @FXML
    private TitledPane titledPane;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableColumn<Repas, Void> colDelet;


    @FXML
    private Button btnSearch;
    @FXML
    private TableColumn<Repas, Void> colUpdate;
    @FXML
    void initialize() {
        try {
            serviceRepas repasService = new serviceRepas();

            // Charger tous les repas
            List<Repas> repasList = repasService.getAll();
            ObservableList<Repas> obse = FXCollections.observableList(repasList);
            tableStudent.setItems(obse);

            // Définir les valeurs des colonnes
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colNbr.setCellValueFactory(new PropertyValueFactory<>("nbRepas"));
            colCout.setCellValueFactory(new PropertyValueFactory<>("cout"));

            // Configurer la cellFactory pour la colonne "Modifier"
            colUpdate.setCellFactory(param -> {
                TableCell<Repas, Void> cell = new TableCell<Repas, Void>() {
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
                            Repas repas = getTableView().getItems().get(getIndex());
                            handleEditAction(repas, event); // Passer l'événement en paramètre
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
                TableCell<Repas, Void> cell = new TableCell<Repas, Void>() {
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
                            Repas repas = getTableView().getItems().get(getIndex());
                            handleDeleteAction(repas);
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
            // Gérer les erreurs
            System.err.println("Erreur lors du chargement des repas : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void searchRepas(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();
        serviceRepas serviceRepas = new serviceRepas();

        try {
            List<Repas> Repas;
            if (searchTerm.isEmpty()) {
                Repas = serviceRepas.getAll();
            } else {
                Repas = serviceRepas.chercherParNom(searchTerm);
            }

            ObservableList<Repas> observableList = FXCollections.observableArrayList(Repas);

            tableStudent.setItems(observableList);

        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des Repas : " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    void addRepas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/ajouterRepas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter Repas");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }
    }

    private void handleDeleteAction(Repas repas) {
        System.out.println("supprimer  la reclamation : " + repas.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/SupprimerRepas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage popupStage = new Stage();
            SupprimerRepas controller = loader.getController();
            controller.initializeData(repas);

            popupStage.setTitle("Popup");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(scene);

            SupprimerRepas popupController = loader.getController();
            popupController.setPopupStage(popupStage);

            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la popup.");
        }

    }

    @FXML
    private void gochat(ActionEvent event) {
        try {
            // Charger la nouvelle interface Chatbot.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/ChatBot.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et la remplacer par la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Chatbot");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleEditAction(Repas repas, ActionEvent event) {
        System.out.println("Modifier le repas : " + repas.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Repas/ModifierRepas.fxml"));
            Parent root = loader.load();

            // Passer le repas au contrôleur de la page modifierRepas
            ModifierRepas controller = loader.getController();
            controller.initializeData(repas);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modifier Repas");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page.");
        }
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
