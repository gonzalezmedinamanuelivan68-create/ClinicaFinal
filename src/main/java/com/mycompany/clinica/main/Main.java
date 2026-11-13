/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;
/**
 *
 * @author Usuario
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/clinica/vistas/Login.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Sistema de Gestión de Clínica");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); 
            primaryStage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la vista Login.fxml. Verifica que el archivo exista en la ruta correcta.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

