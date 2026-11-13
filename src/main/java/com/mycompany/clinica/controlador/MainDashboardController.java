/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clinica.controlador;

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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainDashboardController implements Initializable {

    @FXML private Button btnPacientes;
    @FXML private Button btnDoctores;
    @FXML private Button btnCitas;
    @FXML private Button btnConsultas;
    @FXML private Button btnAdministradores;
    @FXML private Button btnBaseDatos;
    @FXML private Button btnCerrarSesion;
    
    @FXML private StackPane contenedorPrincipal;

    private final String RUTA_VISTAS = "/com/mycompany/clinica/vistas/";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        if (btnPacientes != null) btnPacientes.setOnAction(e -> cargarVista("Paciente.fxml"));
        if (btnDoctores != null) btnDoctores.setOnAction(e -> cargarVista("Doctor.fxml"));
        if (btnCitas != null) btnCitas.setOnAction(e -> cargarVista("Cita.fxml"));
        if (btnConsultas != null) btnConsultas.setOnAction(e -> cargarVista("Consulta.fxml"));
        if (btnBaseDatos != null) btnBaseDatos.setOnAction(e -> cargarVista("ConfiguracionDB.fxml"));
        if (btnAdministradores != null) btnAdministradores.setOnAction(e -> cargarVista("Administrador.fxml"));
        if (btnCerrarSesion != null) btnCerrarSesion.setOnAction(this::cerrarSesion);
    }    

    private void cargarVista(String archivoFxml) {
        try {
            URL ruta = getClass().getResource(RUTA_VISTAS + archivoFxml);
            
            if (ruta == null) {
                mostrarAlerta("Error de Ruta", "No se encontró el archivo FXML: " + RUTA_VISTAS + archivoFxml, Alert.AlertType.ERROR);
                return;
            }
            
            Parent vista = FXMLLoader.load(ruta);
            
            
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(vista);
            
        } catch (Exception ex) {
            mostrarAlerta("Error de Carga", "El archivo " + archivoFxml + " falló al cargar.\nRevisa que su controlador exista y esté bien escrito.", Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    private void cerrarSesion(ActionEvent event) {
        try {
            URL rutaLogin = getClass().getResource(RUTA_VISTAS + "Login.fxml");
            
            if (rutaLogin == null) {
                mostrarAlerta("Error de Ruta", "No se encontró el Login en: " + RUTA_VISTAS + "Login.fxml", Alert.AlertType.ERROR);
                return;
            }

          
            Parent root = FXMLLoader.load(rutaLogin);
            Stage loginStage = new Stage();
            loginStage.setTitle("Sistema de Gestión de Clínica");
            loginStage.setScene(new Scene(root));
            loginStage.setResizable(false);
            loginStage.show();
            
        
            Stage dashboardStage = (Stage) btnCerrarSesion.getScene().getWindow();
            dashboardStage.close();
            
        } catch (Exception ex) {
            mostrarAlerta("Error Crítico", "Ocurrió un error al intentar cerrar sesión.", Alert.AlertType.ERROR);
            ex.printStackTrace();
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