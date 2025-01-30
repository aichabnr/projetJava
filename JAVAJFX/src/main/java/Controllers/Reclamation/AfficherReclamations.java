package Controllers.Reclamation;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class AfficherReclamations {

    @FXML
    private Button btnAddStudent;

    @FXML
    private TableColumn<?, ?> colActions;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colObjet;

    @FXML
    private TableView<?> tableStudent;

    @FXML
    private TitledPane titledPane;

    @FXML
    private TextField txtSearch;

}
