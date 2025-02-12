package Controllers;

import Entites.Repas;
import Services.ServiceFinance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FinanceController {
    @FXML private TableView<Repas> repasTable;
    @FXML private TableColumn<Repas, Integer> idColumn;
    @FXML private TableColumn<Repas, Date> dateColumn;
    @FXML private TableColumn<Repas, String> nomColumn;
    @FXML private TableColumn<Repas, Integer> nbRepasColumn;
    @FXML private TableColumn<Repas, Double> coutColumn;
    @FXML private TableColumn<Repas, Double> revenueColumn;
    @FXML private TableColumn<Repas, Double> revenueColumn1;
    @FXML private Label totalCostLabel;
    @FXML private Label totalMealsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Button exportButton;
    @FXML private Button visualizeButton;
    @FXML private Button visualizeRepasButton;
    @FXML private BarChart<String, Number> expenseRevenueChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private PieChart mealDistributionChart;

    private ObservableList<Repas> repasList = FXCollections.observableArrayList();
    private ServiceFinance serviceFinance;

    public FinanceController() {
        try {
            serviceFinance = new ServiceFinance();
        } catch (SQLException e) {
            showAlert("Initialization Error", "Erreur D'initialisation: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadRepasData();
        updateResume();
        setupExpenseRevenueChart();
        setupMealDistributionChart();
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

        revenueColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getRevenue()).asObject());

        revenueColumn1.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());

        coutColumn.setCellFactory(column -> new TableCell<Repas, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f TND", item));
                }
            }
        });

        revenueColumn.setCellFactory(column -> new TableCell<Repas, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f TND", item));
                }
            }
        });

        revenueColumn1.setCellFactory(column -> new TableCell<Repas, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f TND", item));
                }
            }
        });
    }

    private void loadRepasData() {
        try {
            repasList.clear();
            repasList.addAll(serviceFinance.getAll());
            repasTable.setItems(repasList);
        } catch (SQLException e) {
            showAlert("Database Error", "Erreur du chargement de données: " + e.getMessage());
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
                writer.println("ID,Date,Nom,NbRepas,Cout,Prix,Revenue");
                for (Repas repas : repasList) {
                    writer.printf("%d,%s,%s,%d,%.2f,%.2f,%.2f%n",
                            repas.getId(),
                            repas.getDate(),
                            repas.getNom(),
                            repas.getNbRepas(),
                            repas.getCout(),
                            repas.getPrix(),
                            repas.getRevenue());
                }
                showAlert("Export Success", "Donnée exporté.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Export Error", "Erreur d'export: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleVisualize(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Analyse des revenus et dépenses");

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Revenus et Dépenses");

            XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
            revenueSeries.setName("Revenus");
            XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
            expenseSeries.setName("Dépenses");

            List<Repas> sortedList = new ArrayList<>(repasList);
            sortedList.sort(Comparator.comparing(Repas::getDate));

            for (Repas repas : sortedList) {
                String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(repas.getDate());
                double revenue = repas.getPrix() * repas.getNbRepas();
                double expense = repas.getCout();

                revenueSeries.getData().add(new XYChart.Data<>(dateStr, revenue));
                expenseSeries.getData().add(new XYChart.Data<>(dateStr, expense));
            }

            lineChart.getData().addAll(revenueSeries, expenseSeries);

            VBox vbox = new VBox(10);
            vbox.getChildren().add(lineChart);

            double totalRevenue = serviceFinance.getTotalRevenue();
            double totalCost = serviceFinance.getTotalCost();
            double profit = totalRevenue - totalCost;

            Label profitLabel = new Label(String.format("Total de profit: %.2f TND", profit));
            vbox.getChildren().add(profitLabel);

            Scene scene = new Scene(vbox, 800, 600);
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            showAlert("Erreur de Visualisation", "Erreur lors de la création de la visualisation : " + e.getMessage());
        }
    }


    @FXML
    private void handleVisualizeRepas(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Repas Ajoute");

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
            stackedBarChart.setTitle("List Repas ajoute ");

            Map<String, XYChart.Series<String, Number>> mealSeries = new HashMap<>();

            List<Repas> sortedList = new ArrayList<>(repasList);
            sortedList.sort((r1, r2) -> r1.getDate().compareTo(r2.getDate()));

            for (Repas repas : sortedList) {
                String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(repas.getDate());
                String mealName = repas.getNom();

                mealSeries.computeIfAbsent(mealName, k -> {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(k);
                    return series;
                }).getData().add(new XYChart.Data<>(dateStr, repas.getNbRepas()));
            }

            stackedBarChart.getData().addAll(mealSeries.values());

            VBox vbox = new VBox(10);
            vbox.getChildren().add(stackedBarChart);

            Label totalMealsLabel = new Label(String.format("Total Meals Served: %d",
                    serviceFinance.getTotalMeals()));
            vbox.getChildren().add(totalMealsLabel);

            Scene scene = new Scene(vbox, 800, 600);
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            showAlert("Visualization Error", "Error creating visualization: " + e.getMessage());
        }
    }

    private void updateResume() {
        try {
            double totalCost = serviceFinance.getTotalCost();
            int totalMeals = serviceFinance.getTotalMeals();
            double totalRevenue = serviceFinance.getTotalRevenue();
            totalCostLabel.setText(String.format("Total Cout: %.2f TND", totalCost));
            totalMealsLabel.setText(String.format("Total Repas: %d", totalMeals));
            totalRevenueLabel.setText(String.format("Total Revenue: %.2f TND", totalRevenue));
        } catch (SQLException e) {
            showAlert("Database Error", "Error erreur du calclul: " + e.getMessage());
        }
    }

    private void setupExpenseRevenueChart() {
        try {
            xAxis.setLabel("Category");
            yAxis.setLabel("Montant (TND)");

            double totalCost = serviceFinance.getTotalCost();
            double totalRevenue = serviceFinance.getTotalRevenue();

            ObservableList<XYChart.Series<String, Number>> chartData =
                    FXCollections.observableArrayList();

            XYChart.Series<String, Number> series =
                    new XYChart.Series<>("Resumé",
                            FXCollections.observableArrayList(
                                    new XYChart.Data<>("Total Cout", totalCost),
                                    new XYChart.Data<>("Total Revenue", totalRevenue)
                            ));

            expenseRevenueChart.getData().clear();
            expenseRevenueChart.getData().add(series);
        } catch (SQLException e) {
            showAlert("Database Error", "Error chargement du chart: " + e.getMessage());
        }
    }

    private void setupMealDistributionChart() {
        try {
            List<Repas> repasList = serviceFinance.getAll();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Repas repas : repasList) {
                pieChartData.add(new PieChart.Data(repas.getNom(), repas.getNbRepas()));
            }

            mealDistributionChart.setData(pieChartData);
            mealDistributionChart.setTitle("Distribution des repas");
        } catch (SQLException e) {
            showAlert("Database Error", "Erreur chargement du donnée: " + e.getMessage());
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