package Controllers;

import Entites.Repas;
import Services.serviceRepas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public class RepasController {

    @FXML private TableView<Repas> repasTable;
    @FXML private TableColumn<Repas, Integer> idColumn;
    @FXML private TableColumn<Repas, Date> dateColumn;
    @FXML private TableColumn<Repas, String> nomColumn;
    @FXML private TableColumn<Repas, Integer> nbRepasColumn;
    @FXML private TableColumn<Repas, Double> coutColumn;

    @FXML private TextField idField;
    @FXML private DatePicker dateField;
    @FXML private TextField nomField;
    @FXML private TextField nbRepasField;
    @FXML private TextField coutField;
    @FXML private TextField searchField;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button exportButton;
    @FXML private Button clearButton;

    @FXML private Label totalCostLabel;
    @FXML private Label totalMealsLabel;

    private ObservableList<Repas> repasList = FXCollections.observableArrayList();
    private FilteredList<Repas> filteredList;
    private serviceRepas serviceRepas;

    public RepasController() {
        serviceRepas = new serviceRepas();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupSearch();
        setupTableSelection();
        loadRepasData();
        updateSummary();
    }

    private void loadRepasData() {
        try {
            repasList.clear();
            repasList.addAll(serviceRepas.getAll());
        } catch (SQLException e) {
            showAlert("Database Error", "pas de repas: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        dateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
        nomColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        nbRepasColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNbRepas()).asObject());
        coutColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getCout()).asObject());

        idColumn.setSortable(true);
        dateColumn.setSortable(true);
        nomColumn.setSortable(true);
        nbRepasColumn.setSortable(true);
        coutColumn.setSortable(true);

        coutColumn.setCellFactory(column -> new TableCell<Repas, Double>() {
            @Override
            protected void updateItem(Double cout, boolean empty) {
                super.updateItem(cout, empty);
                if (empty || cout == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f TND", cout));
                }
            }
        });
    }

    private void setupSearch() {
        filteredList = new FilteredList<>(repasList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(repas -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return repas.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(repas.getId()).contains(lowerCaseFilter);
            });
            updateSummary();
        });
        repasTable.setItems(filteredList);
    }

    private void setupTableSelection() {
        repasTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            Repas newRepas = createRepasFromInput();
            serviceRepas.ajouter(newRepas);
            loadRepasData();
            clearFields();
            updateSummary();
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", "erreur d ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Repas selectedRepas = repasTable.getSelectionModel().getSelectedItem();
        if (selectedRepas != null) {
            if (!validateInput()) {
                return;
            }

            try {
                Repas updatedRepas = createRepasFromInput();

                serviceRepas.update(updatedRepas);

                loadRepasData();

                clearFields();
                updateSummary();
            } catch (SQLException | NumberFormatException e) {
                showAlert("Update Error", "erreur de MAJ: " + e.getMessage());
            }
        } else {
            showAlert("Pas de  Selection", "selectionner un repas.");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Repas selectedRepas = repasTable.getSelectionModel().getSelectedItem();
        if (selectedRepas != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("sur de suppression?");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean deleted = serviceRepas.supprimer(selectedRepas.getId());

                    if (deleted) {
                        loadRepasData();
                        clearFields();
                        updateSummary();
                    } else {
                        showAlert("Delete Error", "erreur de suppression.");
                    }
                } catch (SQLException e) {
                    showAlert("Database Error", "erreur de supprime: " + e.getMessage());
                }
            }
        } else {
            showAlert("No Selection", "selectionner un repas.");
        }
    }

    @FXML
    private void handleExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(repasTable.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Date,Nom,NbRepas,Cout");
                for (Repas repas : repasList) {
                    writer.printf("%d,%s,%s,%d,%.2f%n",
                            repas.getId(),
                            repas.getDate(),
                            repas.getNom(),
                            repas.getNbRepas(),
                            repas.getCout());
                }
                showAlert("Export Success", "export success.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Export Error", "Error d'export: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        if (idField.getText().isEmpty() || dateField.getValue() == null ||
                nomField.getText().isEmpty() || nbRepasField.getText().isEmpty() ||
                coutField.getText().isEmpty()) {
            showAlert("Missing Data", "remplisser tous les champs.");
            return false;
        }

        try {
            Integer.parseInt(idField.getText());
            Integer.parseInt(nbRepasField.getText());
            Double.parseDouble(coutField.getText());
            return true;
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "il doit etre un nombre.");
            return false;
        }
    }

    private Repas createRepasFromInput() {
        int id = Integer.parseInt(idField.getText());
        Date date = java.sql.Date.valueOf(dateField.getValue());
        String nom = nomField.getText();
        int nbRepas = Integer.parseInt(nbRepasField.getText());
        double cout = Double.parseDouble(coutField.getText());

        return new Repas(id, date, nom, nbRepas, cout);
    }

    private void populateFields(Repas repas) {
        idField.setText(String.valueOf(repas.getId()));
        dateField.setValue(((java.sql.Date) repas.getDate()).toLocalDate());
        nomField.setText(repas.getNom());
        nbRepasField.setText(String.valueOf(repas.getNbRepas()));
        coutField.setText(String.valueOf(repas.getCout()));
    }

    @FXML
    private void clearFields() {
        idField.clear();
        dateField.setValue(null);
        nomField.clear();
        nbRepasField.clear();
        coutField.clear();
        repasTable.getSelectionModel().clearSelection();
    }

    private void updateSummary() {
        try {
            double totalCost = serviceRepas.getTotalCost();
            int totalMeals = serviceRepas.getTotalMeals();

            totalCostLabel.setText(String.format("Total Cout: %.2f TND", totalCost));
            totalMealsLabel.setText(String.format("Total Repas: %d", totalMeals));
        } catch (SQLException e) {
            showAlert("Database Error", "error de calcul de somme: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.WARNING);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}