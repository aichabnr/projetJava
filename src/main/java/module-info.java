module com.esprit.espritrestau {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires itextpdf;

    requires mysql.connector.j;
    requires org.apache.poi.ooxml;

    opens com.esprit.espritrestau to javafx.fxml;
    opens com.esprit.espritrestau.controllers to javafx.fxml;
    opens com.esprit.espritrestau.entities to javafx.base;

    exports com.esprit.espritrestau;
}
