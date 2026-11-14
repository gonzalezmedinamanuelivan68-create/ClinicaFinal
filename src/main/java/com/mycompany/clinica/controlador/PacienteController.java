package com.mycompany.clinica.controlador;

import com.mycompany.clinica.config.GestorArchivos;
import com.mycompany.clinica.daos.PacienteDAO;
import com.mycompany.clinica.modelo.Paciente;
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

public class PacienteController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEdad;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtRutaImagen;
    @FXML private ImageView imgVistaPrevia;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, Integer> colId;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, String> colApellidos;
    @FXML private TableColumn<Paciente, Integer> colEdad;
    @FXML private TableColumn<Paciente, String> colTelefono;

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private Paciente pacienteSeleccionado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        actualizarTabla();

        if (btnGuardar != null) btnGuardar.setOnAction(e -> guardarPaciente());
        if (btnActualizar != null) btnActualizar.setOnAction(e -> actualizarPaciente());
        if (btnLimpiar != null) btnLimpiar.setOnAction(e -> limpiarCampos());
        if (btnEliminar != null) btnEliminar.setOnAction(e -> eliminarPaciente());

        tablaPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                pacienteSeleccionado = newSel;
                mostrarDetallePaciente(newSel);
            }
        });
    }

    @FXML
    private void btnSeleccionarImagenOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto del Paciente");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );
        File archivoSeleccionado = fileChooser.showOpenDialog(txtNombre.getScene().getWindow());
        if (archivoSeleccionado != null) {
            txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());
            cargarImagenVistaPrevia(archivoSeleccionado.getAbsolutePath());
        }
    }

    private void guardarPaciente() {
        if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() ||
            txtEdad.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Por favor, llena todos los campos del paciente.", Alert.AlertType.WARNING);
            return;
        }

        try {
            String rutaGuardada = GestorArchivos.guardarImagen(txtRutaImagen.getText().trim());

            Paciente p = new Paciente();
            p.setNombre(txtNombre.getText().trim());
            p.setApellidos(txtApellidos.getText().trim());
            p.setEdad(Integer.parseInt(txtEdad.getText().trim()));
            p.setTelefono(txtTelefono.getText().trim());
            p.setRutaFoto(rutaGuardada);

            if (pacienteDAO.guardar(p)) {
                actualizarTabla();
                limpiarCampos();
                mostrarAlerta("Registro exitoso", "Paciente guardado y foto almacenada en Documentos/ClinicaApp/imagenes.", Alert.AlertType.INFORMATION);
            } else {
                GestorArchivos.eliminarImagen(rutaGuardada);
                mostrarAlerta("Error", "No se pudo guardar el paciente.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido", "La edad debe ser un número entero.", Alert.AlertType.WARNING);
        }
    }

    private void actualizarPaciente() {
        if (pacienteSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Selecciona un paciente de la tabla para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            String rutaAnterior = pacienteSeleccionado.getRutaFoto();
            String rutaNueva = GestorArchivos.guardarImagen(txtRutaImagen.getText().trim());

            pacienteSeleccionado.setNombre(txtNombre.getText().trim());
            pacienteSeleccionado.setApellidos(txtApellidos.getText().trim());
            pacienteSeleccionado.setEdad(Integer.parseInt(txtEdad.getText().trim()));
            pacienteSeleccionado.setTelefono(txtTelefono.getText().trim());
            pacienteSeleccionado.setRutaFoto(rutaNueva);

            if (pacienteDAO.actualizar(pacienteSeleccionado)) {
                if (rutaAnterior != null && !rutaAnterior.equals(rutaNueva)) {
                    GestorArchivos.eliminarImagen(rutaAnterior);
                }
                actualizarTabla();
                limpiarCampos();
                mostrarAlerta("Actualizado", "Datos del paciente actualizados correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el registro.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido", "La edad debe ser un número entero.", Alert.AlertType.WARNING);
        }
    }

    private void eliminarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String rutaFoto = seleccionado.getRutaFoto();
            if (pacienteDAO.borrar(seleccionado.getIdPaciente())) {
                GestorArchivos.eliminarImagen(rutaFoto);
                actualizarTabla();
                limpiarCampos();
                mostrarAlerta("Eliminado", "El paciente y su imagen fueron eliminados.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo eliminar el registro.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Selección requerida", "Selecciona un paciente de la tabla.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDetallePaciente(Paciente p) {
        txtNombre.setText(p.getNombre());
        txtApellidos.setText(p.getApellidos());
        txtEdad.setText(String.valueOf(p.getEdad()));
        txtTelefono.setText(p.getTelefono());
        txtRutaImagen.setText(p.getRutaFoto());
        cargarImagenVistaPrevia(p.getRutaFoto());
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
        tablaPacientes.setItems(pacienteDAO.obtenerTodos());
    }

    private void limpiarCampos() {
        pacienteSeleccionado = null;
        txtNombre.clear();
        txtApellidos.clear();
        txtEdad.clear();
        txtTelefono.clear();
        txtRutaImagen.clear();
        imgVistaPrevia.setImage(null);
        tablaPacientes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}