module cr.ac.ucr.paraiso.ie.bases_examenll {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;


    opens cr.ac.ucr.paraiso.ie.bases_examenll.client to javafx.fxml;
    exports cr.ac.ucr.paraiso.ie.bases_examenll;
}