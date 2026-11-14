package com.mycompany.clinica.controlador;

import com.mycompany.clinica.config.Sesion;
import com.mycompany.clinica.modelos.Administrador;
import com.mycompany.clinica.modelos.ERoles;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainDashboardController implements Initializable {

    
    @FXML private Pane panelPrincipal;

 
    @FXML private Button btnAdministradores;
    @FXML private Button btnDoctores;
    @FXML private Button btnPacientes;
    @FXML private Button btnCitas;
    @FXML private Button btnConsultas;
    @FXML private Button btnCerrarSesion;

    @FXML private Label lblUsuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if (btnAdministradores != null) btnAdministradores.setOnAction(this::btnAdministradoresOnAction);
        if (btnDoctores != null) btnDoctores.setOnAction(this::btnDoctoresOnAction);
        if (btnPacientes != null) btnPacientes.setOnAction(this::btnPacientesOnAction);
        if (btnCitas != null) btnCitas.setOnAction(this::btnCitasOnAction);
        if (btnConsultas != null) btnConsultas.setOnAction(this::btnConsultasOnAction);
        if (btnCerrarSesion != null) btnCerrarSesion.setOnAction(this::btnCerrarSesionOnAction);

        
        aplicarPermisos();

        
        Administrador usuario = Sesion.getUsuarioActual();
        if (usuario != null && lblUsuarioActual != null) {
            lblUsuarioActual.setText(usuario.getNombres() + " [" + usuario.getRol() + "]");
        }

       
 
    }

    private void aplicarPermisos() {
        Administrador usuario = Sesion.getUsuarioActual();
        if (usuario == null || usuario.getRol() == null) {
            return;
        }

        ERoles rol = usuario.getRol();

        switch (rol) {
            case ADMINISTRADOR:
                configurarBoton(btnAdministradores, true);
                configurarBoton(btnDoctores, true);
                configurarBoton(btnPacientes, true);
                configurarBoton(btnCitas, true);
                configurarBoton(btnConsultas, true);
                break;

            case RECEPCIONISTA:
                configurarBoton(btnAdministradores, false);
                configurarBoton(btnDoctores, false);
                configurarBoton(btnPacientes, true);
                configurarBoton(btnCitas, true);
                configurarBoton(btnConsultas, false);
                break;

            case DOCTOR:
                configurarBoton(btnAdministradores, false);
                configurarBoton(btnDoctores, false);
                configurarBoton(btnPacientes, false);
                configurarBoton(btnCitas, true);
                configurarBoton(btnConsultas, true);
                break;
        }
    }

    private void configurarBoton(Button boton, boolean permitido) {
        if (boton != null) {
            boton.setDisable(!permitido);
        }
    }

   

    @FXML
    private void btnAdministradoresOnAction(ActionEvent event) {
        cargarVista("/com/mycompany/clinica/vistas/Administrador.fxml");
    }

    @FXML
    private void btnDoctoresOnAction(ActionEvent event) {
        cargarVista("/com/mycompany/clinica/vistas/Doctor.fxml");
    }

    @FXML
    private void btnPacientesOnAction(ActionEvent event) {
        cargarVista("/com/mycompany/clinica/vistas/Paciente.fxml");
    }

    @FXML
    private void btnCitasOnAction(ActionEvent event) {
        cargarVista("/com/mycompany/clinica/vistas/Cita.fxml");
    }

    @FXML
    private void btnConsultasOnAction(ActionEvent event) {
        cargarVista("/com/mycompany/clinica/vistas/Consulta.fxml");
    }

    @FXML
    private void btnCerrarSesionOnAction(ActionEvent event) {
        Sesion.cerrarSesion();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/clinica/vistas/Login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar Sesión - Clínica");
            loginStage.setScene(new Scene(root));
            loginStage.show();

           
            Stage dashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dashboardStage.close();

        } catch (IOException e) {
            System.err.println("Error al volver al Login: " + e.getMessage());
        }
    }

    private void cargarVista(String rutaFxml) {
        if (panelPrincipal == null) {
            System.err.println("ERROR: 'panelPrincipal' es null. Revisa el fx:id del contenedor central en MainDashboard.fxml");
            return;
        }
        try {
            URL recurso = getClass().getResource(rutaFxml);
            if (recurso == null) {
                System.err.println("ERROR: No se encontró el archivo FXML en la ruta: " + rutaFxml);
                return;
            }
            Parent vista = FXMLLoader.load(recurso);
            
            if (panelPrincipal instanceof BorderPane) {
                ((BorderPane) panelPrincipal).setCenter(vista);
            } else {
                panelPrincipal.getChildren().setAll(vista);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + rutaFxml);
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de Navegación");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la interfaz: " + rutaFxml);
            alerta.showAndWait();
        }
    }
}