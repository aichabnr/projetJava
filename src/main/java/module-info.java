module com.esprit.espritrestau {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.esprit.espritrestau to javafx.fxml;
    opens com.esprit.espritrestau.controllers to javafx.fxml;
    opens com.esprit.espritrestau.entities to javafx.base;

    exports com.esprit.espritrestau;
}
