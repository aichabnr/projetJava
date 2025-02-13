package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Abonnement;
import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.services.AbonnementService;
import com.esprit.espritrestau.services.ConsommateurService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbonnementController {

    @FXML
    private TableView<Abonnement> tableAbonnements;

    @FXML
    private TableColumn<Abonnement, Integer> colId;

    @FXML
    private TableColumn<Abonnement, String> colDateDebut;

    @FXML
    private TableColumn<Abonnement, String> colDateFin;

    @FXML
    private TableColumn<Abonnement, Double> colSolde;

    @FXML
    private TableColumn<Abonnement, Integer> colIdConsomateur;

    @FXML
    private TableColumn<Abonnement, String> actionColumn;

    @FXML
    private MenuButton consommateurInput;

    @FXML
    private Button createAbonButton;

    @FXML
    private DatePicker dateDebutInput;

    @FXML
    private DatePicker dateFinInput;

    @FXML
    private TextField soldeInput;

    @FXML
    private TextField searchIdInput;

    @FXML
    private TextField searchSoldeInput;

    @FXML
    private TextField searchConsommateurInput;

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<Abonnement, String> colConsommateur;

    private final AbonnementService abonnementService = new AbonnementService();
    private final ConsommateurService consommateurService = new ConsommateurService();
    private int selectedConsommateurId;

    public AbonnementController() throws SQLException {
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));

        colConsommateur.setCellValueFactory(cellData -> {
            Abonnement abonnement = cellData.getValue();
            return new SimpleStringProperty(abonnement.getNomConsomateur() + " " + abonnement.getPrenomConsomateur());
        });

        setupActionColumn();
        tableAbonnements.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadAbonnements();
        loadConsommateurs();
    }

    @FXML
    void filterAbonnements(ActionEvent event) {
        String idFilter = searchIdInput.getText();
        String soldeFilter = searchSoldeInput.getText();
        String consommateurFilter = searchConsommateurInput.getText().toLowerCase();

        try {
            List<Abonnement> abonnements = abonnementService.getAllAbonnements();
            ObservableList<Abonnement> filteredList = FXCollections.observableArrayList();

            for (Abonnement abonnement : abonnements) {
                boolean matches = true;

                if (!idFilter.isEmpty()) {
                    if (!String.valueOf(abonnement.getId()).equals(idFilter)) {
                        matches = false;
                    }
                }

                if (!soldeFilter.isEmpty()) {
                    if (abonnement.getSolde() != Double.parseDouble(soldeFilter)) {
                        matches = false;
                    }
                }

                if (!consommateurFilter.isEmpty()) {
                    String fullName = abonnement.getNomConsomateur().toLowerCase() + " " + abonnement.getPrenomConsomateur().toLowerCase();
                    if (!fullName.contains(consommateurFilter)) {
                        matches = false;
                    }
                }

                if (matches) {
                    filteredList.add(abonnement);
                }
            }

            tableAbonnements.setItems(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur est survenue lors du filtrage des abonnements.");
        }
    }
    @FXML
    void downloadAbonnements(ActionEvent event) {
        Document document = new Document();
        try {
            // Spécifiez un chemin absolu pour le fichier PDF
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\farah\\Downloads\\Abonnements.pdf"));
            document.open();

            List<Abonnement> abonnements = abonnementService.getAllAbonnements();
            document.add(new Paragraph("Liste des Abonnements"));

            PdfPTable table = new PdfPTable(5); // Nombre de colonnes
            table.addCell("ID");
            table.addCell("Date Début");
            table.addCell("Date Fin");
            table.addCell("Solde");
            table.addCell("Consommateur");

            for (Abonnement abonnement : abonnements) {
                table.addCell(String.valueOf(abonnement.getId()));
                table.addCell(abonnement.getDateDebut().toString());
                table.addCell(abonnement.getDateFin().toString());
                table.addCell(String.valueOf(abonnement.getSolde()));
                table.addCell(abonnement.getNomConsomateur() + " " + abonnement.getPrenomConsomateur());
            }

            document.add(table);
            showAlert("Succès", "Le fichier PDF a été créé avec succès.");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la création du PDF : " + e.getMessage());
        } finally {
            document.close();
        }
    }
    @FXML
    void exportToExcel(ActionEvent event) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Abonnements");

        // Créer l'en-tête de la feuille
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Date Début");
        headerRow.createCell(2).setCellValue("Date Fin");
        headerRow.createCell(3).setCellValue("Solde");
        headerRow.createCell(4).setCellValue("Consommateur");

        // Remplir les données
        try {
            List<Abonnement> abonnements = abonnementService.getAllAbonnements();
            int rowNum = 1;
            for (Abonnement abonnement : abonnements) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(abonnement.getId());
                dataRow.createCell(1).setCellValue(abonnement.getDateDebut().toString());
                dataRow.createCell(2).setCellValue(abonnement.getDateFin().toString());
                dataRow.createCell(3).setCellValue(abonnement.getSolde());
                dataRow.createCell(4).setCellValue(abonnement.getNomConsomateur() + " " + abonnement.getPrenomConsomateur());
            }

            // Écrire le fichier
            String filePath = "C:\\Users\\farah\\Downloads\\Abonnements.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                showAlert("Succès", "Le fichier Excel a été créé avec succès.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la création du fichier Excel : " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<TableColumn<Abonnement, String>, TableCell<Abonnement, String>>() {
            @Override
            public TableCell<Abonnement, String> call(TableColumn<Abonnement, String> param) {
                return new TableCell<Abonnement, String>() {
                    final Button modifyButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            Abonnement abonnement = getTableView().getItems().get(getIndex());

                            modifyButton.setOnAction(event -> modifyAbonnement(abonnement));
                            deleteButton.setOnAction(event -> deleteAbonnement(abonnement));

                            HBox hbox = new HBox(modifyButton, deleteButton);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void modifyAbonnement(Abonnement abonnement) {
        Dialog<Abonnement> dialog = new Dialog<>();
        dialog.setTitle("Modifier Abonnement");
        dialog.setHeaderText("Modifier les détails de l'abonnement");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField dateDebutField = new TextField(abonnement.getDateDebut().toString());
        TextField dateFinField = new TextField(abonnement.getDateFin().toString());
        TextField soldeField = new TextField(String.valueOf(abonnement.getSolde()));

        final int[] selectedConsommateurId = {abonnement.getIdConsomateur()};
        MenuButton consommateurMenuButton = new MenuButton("Choisir un Consommateur");
        loadConsommateursForDialog(consommateurMenuButton, selectedConsommateurId);
        consommateurMenuButton.setText(abonnement.getNomConsomateur() + " " + abonnement.getPrenomConsomateur());

        grid.add(new Label("Date Début:"), 0, 0);
        grid.add(dateDebutField, 1, 0);
        grid.add(new Label("Date Fin:"), 0, 1);
        grid.add(dateFinField, 1, 1);
        grid.add(new Label("Solde:"), 0, 2);
        grid.add(soldeField, 1, 2);
        grid.add(new Label("ID Consommateur:"), 0, 3);
        grid.add(consommateurMenuButton, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Date dateDebut = Date.valueOf(dateDebutField.getText());
                    Date dateFin = Date.valueOf(dateFinField.getText());
                    double solde = Double.parseDouble(soldeField.getText());
                    int idConsomateur = selectedConsommateurId[0];

                    Abonnement updatedAbonnement = new Abonnement(abonnement.getId(), dateDebut, dateFin, solde, idConsomateur);
                    abonnementService.updateAbonnement(updatedAbonnement);

                    loadAbonnements();
                    showAlert("Success", "Modification enregistrée avec succès.");
                    return updatedAbonnement;
                } catch (Exception e) {
                    showAlert("Errorr", "Erreur lors de la mise à jour de l'abonnement.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void loadConsommateursForDialog(MenuButton menuButton, int[] selectedConsommateurId) {
        menuButton.getItems().clear();

        try {
            List<Consommateur> consommateurs = consommateurService.getAllConsommateurs();
            for (Consommateur consommateur : consommateurs) {
                MenuItem item = new MenuItem(consommateur.getNom() + " " + consommateur.getPrenom());
                item.setOnAction(event -> {
                    selectedConsommateurId[0] = consommateur.getId();
                    menuButton.setText(consommateur.getNom() + " " + consommateur.getPrenom());
                });
                menuButton.getItems().add(item);
            }

            for (Consommateur consommateur : consommateurs) {
                if (consommateur.getId() == selectedConsommateurId[0]) {
                    menuButton.setText(consommateur.getNom() + " " + consommateur.getPrenom());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur s'est produite lors du chargement des consommateurs.");
        }
    }

    private void deleteAbonnement(Abonnement abonnement) {
        try {
            abonnementService.deleteAbonnement(abonnement.getId());
            loadAbonnements();
            showAlert("Success", "Abonnement supprimé avec succès.");
        } catch (Exception e) {
            showAlert("Error", "Une erreur s'est produite lors de la suppression.");
        }
    }

    private void loadAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementService.getAllAbonnements();
            ObservableList<Abonnement> observableList = FXCollections.observableArrayList(abonnements);
            tableAbonnements.setItems(observableList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConsommateurs() {
        try {
            List<Consommateur> consommateurs = consommateurService.getAllConsommateurs();
            for (Consommateur consommateur : consommateurs) {
                MenuItem item = new MenuItem(consommateur.getNom() + " " + consommateur.getPrenom());
                item.setOnAction(event -> handleConsommateurSelection(consommateur));
                consommateurInput.getItems().add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur s'est produite lors du chargement des consommateurs.");
        }
    }

    private void handleConsommateurSelection(Consommateur consommateur) {
        selectedConsommateurId = consommateur.getId();
        consommateurInput.setText(consommateur.getNom() + " " + consommateur.getPrenom());
        System.out.println("Consommateur sélectionné : " + consommateur.getNom() + " " + consommateur.getPrenom());
    }

    @FXML
    void addAbonnement(ActionEvent event) {
        try {
            if (dateDebutInput.getValue() == null || dateFinInput.getValue() == null) {
                showAlert("Error", "Veuillez sélectionner une date de début et une date de fin.");
                return;
            }

            Date dateDebut = java.sql.Date.valueOf(dateDebutInput.getValue());
            Date dateFin = java.sql.Date.valueOf(dateFinInput.getValue());

            double solde;
            try {
                solde = Double.parseDouble(soldeInput.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Veuillez entrer un solde valide.");
                return;
            }

            Abonnement newAbonnement = new Abonnement(0, dateDebut, dateFin, solde, selectedConsommateurId);
            abonnementService.addAbonnement(newAbonnement);
            loadAbonnements();
            showAlert("Success", "Abonnement ajouté avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Une erreur s'est produite lors de l'ajout de l'abonnement.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<Consommateur> getAllConsommateurs() {
        try {
            return consommateurService.getAllConsommateurs();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}