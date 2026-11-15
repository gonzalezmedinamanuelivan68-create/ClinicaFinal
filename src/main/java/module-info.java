module com.mycompany.clinica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
    requires java.net.http;
    requires com.google.gson;
    
    opens com.mycompany.clinica.modelo to com.google.gson, javafx.base;
    opens com.mycompany.clinica.modelos to com.google.gson, javafx.base;

    opens com.mycompany.clinica to javafx.fxml;
    opens com.mycompany.clinica.main to javafx.fxml;
    opens com.mycompany.clinica.controlador to javafx.fxml;
    exports com.mycompany.clinica;
    exports com.mycompany.clinica.main;
    //opens com.mycompany.clinica.modelo to javafx.base;
}
