/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clinica.controlador;

import com.mycompany.clinica.modelo.AlmacenDatos;
import com.mycompany.clinica.modelo.Paciente;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Gonzalez medina Manuel Ivan
 */
public class PacienteController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextField txtTelefono;
    
    @FXML 
    private ImageView imgFoto;
    @FXML private TextField txtRutaImagen;
    @FXML private ImageView imgVistaPrevia;
    @FXML 
    private Button btnSubirFoto;
    private String rutaFotoActual = "";

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnEliminar;

    @FXML
    private TableView<Paciente> tablaPacientes;
    @FXML
    private TableColumn<Paciente, Integer> colId;
    @FXML
    private TableColumn<Paciente, String> colNombre;
    @FXML
    private TableColumn<Paciente, String> colApellidos;
    @FXML
    private TableColumn<Paciente, Integer> colEdad;
    @FXML
    private TableColumn<Paciente, String> colTelefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaPacientes.setItems(AlmacenDatos.getInstancia().getPacientes());

        btnGuardar.setOnAction(e -> guardarPaciente());
        btnLimpiar.setOnAction(e -> limpiarCampos());
        btnEliminar.setOnAction(e -> eliminarPaciente());
        btnSubirFoto.setOnAction(e -> seleccionarFoto());
        
        tablaPacientes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null && nuevo.getRutaFoto() != null && !nuevo.getRutaFoto().isEmpty()) {
                imgFoto.setImage(new Image(nuevo.getRutaFoto()));
            } else {
                imgFoto.setImage(null);
            }
        });
    }
    
    private void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto de Perfil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        
        File archivoSelec = fileChooser.showOpenDialog(null);
        if (archivoSelec != null) {
            rutaFotoActual = archivoSelec.toURI().toString();
            imgFoto.setImage(new Image(rutaFotoActual));
        }
    }

    private void guardarPaciente() {
        try {
            String nombre = txtNombre.getText();
            String apellidos = txtApellidos.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String telefono = txtTelefono.getText();
            
           

            if (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.WARNING);
                return;
            }

            int id = AlmacenDatos.getInstancia().getSiguienteIdPaciente();
            Paciente nuevoPaciente = new Paciente(id, nombre, apellidos, edad, telefono);

            AlmacenDatos.getInstancia().getPacientes().add(nuevoPaciente);

            limpiarCampos();
            mostrarAlerta("Éxito", "Paciente registrado correctamente", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellidos.clear();
        txtEdad.clear();
        txtTelefono.clear();
    }

    private void eliminarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            AlmacenDatos.getInstancia().getPacientes().remove(seleccionado);
            mostrarAlerta("Eliminado", "Paciente eliminado del registro", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Selecciona un paciente de la tabla para eliminar", Alert.AlertType.WARNING);
        }
    }
    @FXML
    private void btnSeleccionarImagenOnAction(javafx.event.ActionEvent event) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Seleccionar Foto del Paciente");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );
        
        java.io.File archivoSeleccionado = fileChooser.showOpenDialog(txtNombre.getScene().getWindow());
        
        if (archivoSeleccionado != null) {
            txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());
            cargarImagenVistaPrevia(archivoSeleccionado.getAbsolutePath());
        }
    }

    // Y asegúrate de tener también el método que dibuja la foto:
    private void cargarImagenVistaPrevia(String ruta) {
        if (ruta != null && !ruta.trim().isEmpty()) {
            java.io.File archivo = new java.io.File(ruta);
            if (archivo.exists()) {
                javafx.scene.image.Image imagen = new javafx.scene.image.Image(archivo.toURI().toString());
                imgVistaPrevia.setImage(imagen);
                return;
            }
        }
        imgVistaPrevia.setImage(null);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
