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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class LoginController implements Initializable {

 @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnIngresar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    @FXML
    private void iniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, ingresa tu usuario y contraseña.", Alert.AlertType.WARNING);
        } else if (usuario.equals("admin") && password.equals("1234")) {
         try {
                // IMPORTANTE: Si le pusiste otro nombre a tu proyecto, cambia "gestionclinica" por el nombre de tu nuevo paquete (ej. "clinicafinal")
                String rutaDashboard = "/com/mycompany/clinica/vistas/MainDashboard.fxml";
                java.net.URL urlVista = getClass().getResource(rutaDashboard);
                
                if (urlVista == null) {
                    mostrarAlerta("Error de Ruta", "No se encontró el archivo FXML en: " + rutaDashboard, Alert.AlertType.ERROR);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(urlVista);
                javafx.scene.Parent root = loader.load();
                
                // Abrir la nueva ventana del Dashboard
                javafx.scene.Scene scene = new javafx.scene.Scene(root);
                javafx.stage.Stage dashboardStage = new javafx.stage.Stage();
                dashboardStage.setTitle("Panel Principal - Sistema de Clínica");
                dashboardStage.setScene(scene);
                dashboardStage.show();
                
                // Cerrar la ventana actual del Login
                javafx.stage.Stage loginStage = (javafx.stage.Stage) btnIngresar.getScene().getWindow();
                loginStage.close();
                
            } catch (Exception e) {
                mostrarAlerta("Error Crítico", "Ocurrió un error al intentar abrir el Dashboard.", Alert.AlertType.ERROR);
                e.printStackTrace(); // Esto imprimirá el error exacto en la consola (letras rojas)
            }
            
        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }   

    private void mostrarAlerta(String campos_vacíos, String por_favor_ingresa_tu_usuario_y_contraseña, Alert.AlertType alertType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
           

