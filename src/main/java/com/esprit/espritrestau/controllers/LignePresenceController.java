package com.esprit.espritrestau.controllers;

import com.esprit.espritrestau.entities.Presence;
import com.esprit.espritrestau.entities.Repas;
import com.esprit.espritrestau.entities.Consommateur;
import com.esprit.espritrestau.services.LignePresenceService;
import com.esprit.espritrestau.utils.DataSource;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LignePresenceController {

    @FXML
    private TableView<Presence> presenceTableView;

    @FXML
    private TableColumn<Presence, Integer> idColumn;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button printButton;

    @FXML
    private TableColumn<Presence, Date> dateColumn;

    @FXML
    private TableColumn<Presence, String> idRepasColumn;

    @FXML
    private TableColumn<Presence, String> idConsomateurColumn;

    @FXML
    private TableColumn<Presence, Void> modifyColumn;

    @FXML
    private TableColumn<Presence, Void> deleteColumn;

    @FXML
    private ComboBox<Repas> repasComboBox;

    @FXML
    private ComboBox<Consommateur> consommateurComboBox;

    @FXML
    private DatePicker dateInput;

    @FXML
    private Button addButton;

    private LignePresenceService service;
    private Connection connection;

    @FXML
    public void initialize() {
        try {
            service = new LignePresenceService();
            connection = DataSource.getConnection();
            loadRepasData();
            loadConsommateurData();
            loadPresenceData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up cell value factories for the TableView columns
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));

        idRepasColumn.setCellValueFactory(cellData -> {
            int repasId = cellData.getValue().getIdRepas();
            Repas repas = service.getRepasById(repasId); // Fetch Repas object
            return new SimpleStringProperty(repas != null ? repas.getNom() : "Unknown");
        });

        idConsomateurColumn.setCellValueFactory(cellData -> {
            int consommateurId = cellData.getValue().getIdConsomateur();
            Consommateur consommateur = service.getConsommateurById(consommateurId); // Fetch Consommateur object
            return new SimpleStringProperty(consommateur != null ? consommateur.getNom() + " " + consommateur.getPrenom() : "Unknown");
        });

        // Initialize modify and delete columns
        setupModifyColumn();
        setupDeleteColumn();

        // Adjust column widths
        adjustColumnWidths();

        // Set up date pickers to enable/disable the print button based on date selection
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> checkDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> checkDates());
    }
    private void checkDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            printButton.setDisable(false);
        } else {
            printButton.setDisable(true);
        }
    }

    @FXML
    private void handlePrintButton() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            List<Presence> presences = service.getPresencesBetweenDates(startDate, endDate);
            printPresencesToPDF(presences);
        }
    }
    private void printPresencesToPDF(List<Presence> presences) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float yPosition = yStart;
            float leading = 14.5f;

            // Title
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("List of Presences");
            contentStream.endText();
            yPosition -= leading * 2;

            // Table Header
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("ID       Date               Repas Name                 Consommateur Name");
            contentStream.endText();
            yPosition -= leading;

            // Draw header border
            drawLine(contentStream, margin, yPosition, margin + 400, yPosition);

            // Add each presence
            for (Presence presence : presences) {
                Repas repas = service.getRepasById(presence.getIdRepas());
                Consommateur consommateur = service.getConsommateurById(presence.getIdConsomateur());

                String repasName = repas != null ? repas.getNom() : "Unknown";
                String consommateurName = consommateur != null ? consommateur.getNom() + " " + consommateur.getPrenom() : "Unknown";

                // Format the line
                String line = String.format("%-8d %-18s %-25s %s",
                        presence.getId(),
                        presence.getDate().toString(),
                        repasName,
                        consommateurName);

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.showText(line);
                contentStream.endText();
                yPosition -= leading;

                // Draw a line below each row
                drawLine(contentStream, margin, yPosition + 2, margin + 400, yPosition + 2);

                // Check for new page
                if (yPosition <= margin) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    yPosition = yStart;

                    // Re-add headers on new page
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("ID       Date               Repas Name                 Consommateur Name");
                    contentStream.endText();
                    yPosition -= leading;
                    drawLine(contentStream, margin, yPosition, margin + 400, yPosition);
                }
            }

            // Close the content stream
            contentStream.close();
            document.save("Presences.pdf");
            System.out.println("PDF created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to draw lines
    private void drawLine(PDPageContentStream contentStream, float x1, float y1, float x2, float y2) throws IOException {
        contentStream.moveTo(x1, y1);
        contentStream.lineTo(x2, y2);
        contentStream.stroke();
    }
    private void setupModifyColumn() {
        modifyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");

            {
                modifyButton.setOnAction(event -> {
                    Presence presence = getTableView().getItems().get(getIndex());
                    handleModifyButton(presence);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });
    }

    private void setupDeleteColumn() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Presence presence = getTableView().getItems().get(getIndex());
                    handleDeleteButton(presence);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }
    private void handleModifyButton(Presence presence) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Presence");
        dialog.setHeaderText("Update Presence Information");

        // Create the dialog pane and set button types
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Custom Content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setPrefWidth(400); // Wider Dialog
        content.setPrefHeight(300); //Taller Dialog

        Label repasLabel = new Label("Select Repas:");
        ComboBox<Repas> updateRepasComboBox = new ComboBox<>();
        updateRepasComboBox.setItems(repasComboBox.getItems());
        updateRepasComboBox.setValue(service.getRepasById(presence.getIdRepas()));
        updateRepasComboBox.setMaxWidth(Double.MAX_VALUE);

        Label consommateurLabel = new Label("Select Consommateur:");
        ComboBox<Consommateur> updateConsommateurComboBox = new ComboBox<>();
        updateConsommateurComboBox.setItems(consommateurComboBox.getItems());
        updateConsommateurComboBox.setValue(service.getConsommateurById(presence.getIdConsomateur()));
        updateConsommateurComboBox.setMaxWidth(Double.MAX_VALUE);

        Label dateLabel = new Label("Select Date:");
        DatePicker updateDatePicker = new DatePicker();
        updateDatePicker.setValue(presence.getDate().toLocalDate());
        updateDatePicker.setMaxWidth(Double.MAX_VALUE);

        content.getChildren().addAll(repasLabel, updateRepasComboBox, consommateurLabel, updateConsommateurComboBox,dateLabel, updateDatePicker);

        // Apply CSS styles to match your application theme
        content.getStyleClass().add("update-dialog");
        dialogPane.setContent(content);

        dialogPane.getStylesheets().add(getClass().getResource("/styles/ligPre-styles.css").toExternalForm());

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LocalDate newDate = updateDatePicker.getValue();
            Repas selectedRepas = updateRepasComboBox.getValue();
            Consommateur selectedConsommateur = updateConsommateurComboBox.getValue();

            if (newDate != null && selectedRepas != null && selectedConsommateur != null) {
                service.update(
                        presence.getId(),
                        Date.valueOf(newDate),
                        selectedRepas.getId(),
                        selectedConsommateur.getId()
                );
                loadPresenceData();
                showAlert("Success", "Presence successfully modified!");
            } else {
                showAlert("Error", "All fields must be filled!");
            }
        }
    }


    private void handleDeleteButton(Presence presence) {
        service.delete(presence.getId());
        loadPresenceData();
        showAlert("Success", "Presence successfully deleted!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadRepasData() {
        List<Repas> repasList = new ArrayList<>();
        String query = "SELECT * FROM repas";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Repas repas = new Repas(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("nom-repas"),
                        rs.getInt("nbrRepas"),
                        rs.getDouble("cout")
                );
                repasList.add(repas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        repasComboBox.getItems().setAll(repasList);
    }

    private void loadConsommateurData() {
        List<Consommateur> consommateurList = new ArrayList<>();
        String query = "SELECT * FROM consommateur";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Consommateur consommateur = new Consommateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("tel"),
                        rs.getString("password"),
                        null
                );
                consommateurList.add(consommateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        consommateurComboBox.getItems().setAll(consommateurList);
    }


    private void loadPresenceData() {
        try{
            List<Presence> presenceList = service.getAll();
            presenceTableView.getItems().setAll(presenceList);

        }catch (RuntimeException e){
            throw new RuntimeException(e) ;
        }
    }

    private void adjustColumnWidths() {
        presenceTableView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                presenceTableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                    double totalWidth = newWidth.doubleValue();
                    double buttonWidth = 100; // Width for each button column
                    double dataColumnsWidth = totalWidth - (2 * buttonWidth);

                    idColumn.setPrefWidth(dataColumnsWidth * 0.25);
                    dateColumn.setPrefWidth(dataColumnsWidth * 0.25);
                    idRepasColumn.setPrefWidth(dataColumnsWidth * 0.25);
                    idConsomateurColumn.setPrefWidth(dataColumnsWidth * 0.25);
                    modifyColumn.setPrefWidth(buttonWidth);
                    deleteColumn.setPrefWidth(buttonWidth);
                });
            }
        });
    }

    @FXML
    public void handleAddButton() {
        Repas selectedRepas = repasComboBox.getValue();
        Consommateur selectedConsommateur = consommateurComboBox.getValue();
        LocalDate localDate = dateInput.getValue();

        if (selectedRepas == null || selectedConsommateur == null || localDate == null) {
            showAlert("Error", "Fill all fields");
            return;
        }

        int idRepas = selectedRepas.getId();
        int idConsomateur = selectedConsommateur.getId();
        Date date = Date.valueOf(localDate);

        Presence newPresence = new Presence();
        newPresence.setIdRepas(idRepas);
        newPresence.setIdConsomateur(idConsomateur);
        newPresence.setDate(date);
        service.add(newPresence);
        loadPresenceData();
    }

}