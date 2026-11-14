package com.mycompany.clinica.controlador;

import com.mycompany.clinica.config.GestorArchivos;
import com.mycompany.clinica.daos.DoctorDAO;
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
    @FXML private TextField txtRutaImagen;
    @FXML private ImageView imgVistaPrevia;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Doctor> tablaDoctores;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colNombre;
    @FXML private TableColumn<Doctor, String> colApellidos;
    @FXML private TableColumn<Doctor, String> colEspecialidad;
    @FXML private TableColumn<Doctor, String> colCedula;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private Doctor doctorSeleccionado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));

        actualizarTabla();

        if (btnGuardar != null) btnGuardar.setOnAction(e -> guardarDoctor());
        if (btnActualizar != null) btnActualizar.setOnAction(e -> actualizarDoctor());
        if (btnLimpiar != null) btnLimpiar.setOnAction(e -> limpiarCampos());
        if (btnEliminar != null) btnEliminar.setOnAction(e -> eliminarDoctor());

        tablaDoctores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                doctorSeleccionado = newSel;
                mostrarDetalleDoctor(newSel);
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

    private void guardarDoctor() {
        if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() ||
            txtEspecialidad.getText().trim().isEmpty() || txtCedula.getText().trim().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Por favor, llena todos los campos del doctor.", Alert.AlertType.WARNING);
            return;
        }

        // Copia la imagen a Documentos/ClinicaApp/imagenes y obtiene la nueva ruta
        String rutaGuardada = GestorArchivos.guardarImagen(txtRutaImagen.getText().trim());

        Doctor doc = new Doctor();
        doc.setNombre(txtNombre.getText().trim());
        doc.setApellidos(txtApellidos.getText().trim());
        doc.setEspecialidad(txtEspecialidad.getText().trim());
        doc.setCedula(txtCedula.getText().trim());
        doc.setRutaFoto(rutaGuardada);

        if (doctorDAO.guardar(doc)) {
            actualizarTabla();
            limpiarCampos();
            mostrarAlerta("Registro exitoso", "Doctor guardado y foto almacenada en Documentos/ClinicaApp/imagenes.", Alert.AlertType.INFORMATION);
        } else {
            GestorArchivos.eliminarImagen(rutaGuardada);
            mostrarAlerta("Error", "No se pudo guardar el doctor en MySQL.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarDoctor() {
        if (doctorSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Selecciona un doctor de la tabla para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        String rutaAnterior = doctorSeleccionado.getRutaFoto();
        String rutaNueva = GestorArchivos.guardarImagen(txtRutaImagen.getText().trim());

        doctorSeleccionado.setNombre(txtNombre.getText().trim());
        doctorSeleccionado.setApellidos(txtApellidos.getText().trim());
        doctorSeleccionado.setEspecialidad(txtEspecialidad.getText().trim());
        doctorSeleccionado.setCedula(txtCedula.getText().trim());
        doctorSeleccionado.setRutaFoto(rutaNueva);

        if (doctorDAO.actualizar(doctorSeleccionado)) {
            // Si cambió la foto, eliminamos el archivo viejo de Documentos
            if (rutaAnterior != null && !rutaAnterior.equals(rutaNueva)) {
                GestorArchivos.eliminarImagen(rutaAnterior);
            }
            actualizarTabla();
            limpiarCampos();
            mostrarAlerta("Actualizado", "Datos del doctor actualizados correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el registro.", Alert.AlertType.ERROR);
        }
    }

    private void eliminarDoctor() {
        Doctor seleccionado = tablaDoctores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String rutaFoto = seleccionado.getRutaFoto();
            if (doctorDAO.borrar(seleccionado.getIdDoctor())) {
                GestorArchivos.eliminarImagen(rutaFoto);
                actualizarTabla();
                limpiarCampos();
                mostrarAlerta("Eliminado", "El doctor y su imagen fueron eliminados.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo eliminar el registro.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Selección requerida", "Selecciona un doctor de la tabla para eliminarlo.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDetalleDoctor(Doctor doctor) {
        txtNombre.setText(doctor.getNombre());
        txtApellidos.setText(doctor.getApellidos());
        txtEspecialidad.setText(doctor.getEspecialidad());
        txtCedula.setText(doctor.getCedula());
        txtRutaImagen.setText(doctor.getRutaFoto());
        cargarImagenVistaPrevia(doctor.getRutaFoto());
    }

    private void cargarImagenVistaPrevia(String ruta) {
        if (ruta != null && !ruta.trim().isEmpty()) {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                imgVistaPrevia.setImage(new Image(archivo.toURI().toString()));
                return;
            }
        }
        imgVistaPrevia.setImage(null);
    }

    private void actualizarTabla() {
        tablaDoctores.setItems(doctorDAO.obtenerTodos());
    }

    private void limpiarCampos() {
        doctorSeleccionado = null;
        txtNombre.clear();
        txtApellidos.clear();
        txtEspecialidad.clear();
        txtCedula.clear();
        txtRutaImagen.clear();
        imgVistaPrevia.setImage(null);
        tablaDoctores.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}