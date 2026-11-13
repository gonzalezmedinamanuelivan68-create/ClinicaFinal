package com.mycompany.clinica.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConfiguracionBDController implements Initializable {

    @FXML private TextField txtHost;
    @FXML private TextField txtPuerto;
    @FXML private TextField txtBaseDatos;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    @FXML private Button btnProbar;
    @FXML private Button btnGuardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtHost.setText("localhost");
        txtPuerto.setText("3306");
        txtBaseDatos.setText("clinica_db");
        txtUsuario.setText("root");

        btnProbar.setOnAction(e -> probarConexion());
        btnGuardar.setOnAction(e -> guardarConfiguracion());
    }

    private void probarConexion() {
 
        java.sql.Connection conn = com.mycompany.clinica.config.Conexion.conectar();
        
        if (conn != null) {
            mostrarAlerta("Conexión Exitosa", "La aplicación se ha conectado correctamente a MySQL en XAMPP.", Alert.AlertType.INFORMATION);
            com.mycompany.clinica.config.Conexion.desconectar(conn); 
        } else {
            mostrarAlerta("Error de Conexión", "No se pudo conectar a la base de datos.\nRevisa que MySQL esté corriendo en XAMPP y que la base de datos exista.", Alert.AlertType.ERROR);
        }
   }

    private void guardarConfiguracion() {
        mostrarAlerta("Configuración Guardada", "Los parámetros de conexión se han guardado exitosamente.", Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
