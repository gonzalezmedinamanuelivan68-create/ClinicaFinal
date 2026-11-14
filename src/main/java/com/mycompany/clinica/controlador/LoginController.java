package com.mycompany.clinica.controlador;

import com.mycompany.clinica.config.Sesion;
import com.mycompany.clinica.daos.AdministradorDAO;
import com.mycompany.clinica.modelos.Administrador;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private TextInputControl txtContraseña; 
    @FXML private Button btnIniciarSesion;

    private final AdministradorDAO administradorDAO = new AdministradorDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (txtContraseña != null) {
            txtContraseña.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    btnIniciarSesion(null);
                }
            });
        }
    }

    @FXML
    private void btnIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String password = txtContraseña.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, ingresa tu usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }


        Administrador usuarioLogueado = administradorDAO.login(usuario, password);

        if (usuarioLogueado != null) {

            Sesion.setUsuarioActual(usuarioLogueado);

            abrirDashboard();
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos, o la cuenta está inactiva.", Alert.AlertType.ERROR);
            txtContraseña.clear();
            txtUsuario.requestFocus();
        }
    }

    private void abrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/clinica/MainDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Sistema de Gestión Clínica - Sesión: " + Sesion.getUsuarioActual().getNombres() + " (" + Sesion.getUsuarioActual().getRol() + ")");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            Stage loginStage = (Stage) txtUsuario.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            System.err.println("Error al cargar MainDashboard.fxml: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "No se pudo abrir la ventana principal. Revisa la ruta del FXML.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}