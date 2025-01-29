module com.esprit.espritrestau {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;


    opens com.esprit.espritrestau to javafx.fxml;
    exports com.esprit.espritrestau;
}