package com.mycompany.clinica.controlador;

import com.mycompany.clinica.modelo.AlmacenDatos;
import com.mycompany.clinica.modelo.Doctor;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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

public class DoctorController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtCedula;
    @FXML private TextField txtRutaImagen; // Nuevo campo
    @FXML private ImageView imgVistaPrevia; // Nueva previsualización

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Doctor> tablaDoctores;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colNombre;
    @FXML private TableColumn<Doctor, String> colApellidos;
    @FXML private TableColumn<Doctor, String> colEspecialidad;
    @FXML private TableColumn<Doctor, String> colCedula;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));

        tablaDoctores.setItems(AlmacenDatos.getInstancia().getDoctores());

        btnGuardar.setOnAction(e -> guardarDoctor());
        btnLimpiar.setOnAction(e -> limpiarCampos());
        btnEliminar.setOnAction(e -> eliminarDoctor());
        
        // Listener para mostrar los datos y la foto al seleccionar un doctor de la tabla
        tablaDoctores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleDoctor(newSelection);
            }
        });
    }

    @FXML
    private void btnSeleccionarImagenOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto del Doctor");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );
        File archivoSeleccionado = fileChooser.showOpenDialog(txtNombre.getScene().getWindow());
        if (archivoSeleccionado != null) {
            txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());
            cargarImagenVistaPrevia(archivoSeleccionado.getAbsolutePath());
        }
    }

    private void cargarImagenVistaPrevia(String ruta) {
        if (ruta != null && !ruta.trim().isEmpty()) {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                Image imagen = new Image(archivo.toURI().toString());
                imgVistaPrevia.setImage(imagen);
                return;
            }
        }
        imgVistaPrevia.setImage(null);
    }

    private void mostrarDetalleDoctor(Doctor doctor) {
        txtNombre.setText(doctor.getNombre());
        txtApellidos.setText(doctor.getApellidos());
        txtEspecialidad.setText(doctor.getEspecialidad());
        txtCedula.setText(doctor.getCedula());
        txtRutaImagen.setText(doctor.getRutaFoto());
        cargarImagenVistaPrevia(doctor.getRutaFoto());
    }

    private void guardarDoctor() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String especialidad = txtEspecialidad.getText();
        String cedula = txtCedula.getText();
        String rutaFoto = txtRutaImagen.getText(); // Extraer foto

        if (nombre.isEmpty() || apellidos.isEmpty() || especialidad.isEmpty() || cedula.isEmpty()) {
            mostrarAlerta("Campos incompletos", "Por favor, llena todos los campos del doctor.", Alert.AlertType.WARNING);
            return;
        }

        int id = AlmacenDatos.getInstancia().getSiguienteIdDoctor();
        Doctor nuevoDoctor = new Doctor(id, nombre, apellidos, especialidad, cedula);
        nuevoDoctor.setRutaFoto(rutaFoto); // Asignar foto

        AlmacenDatos.getInstancia().getDoctores().add(nuevoDoctor);

        limpiarCampos();
        mostrarAlerta("Registro exitoso", "El doctor ha sido registrado en el sistema.", Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellidos.clear();
        txtEspecialidad.clear();
        txtCedula.clear();
        txtRutaImagen.clear();
        imgVistaPrevia.setImage(null);
        tablaDoctores.getSelectionModel().clearSelection();
    }

    private void eliminarDoctor() {
        Doctor seleccionado = tablaDoctores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            AlmacenDatos.getInstancia().getDoctores().remove(seleccionado);
            limpiarCampos();
            mostrarAlerta("Eliminado", "El registro del doctor ha sido eliminado.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selección requerida", "Por favor, selecciona un doctor de la tabla para eliminarlo.", Alert.AlertType.WARNING);
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