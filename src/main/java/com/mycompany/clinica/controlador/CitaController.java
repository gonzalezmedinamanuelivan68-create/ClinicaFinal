package com.mycompany.clinica.controlador;

import com.mycompany.clinica.daos.CitaDAO;
import com.mycompany.clinica.daos.DoctorDAO;
import com.mycompany.clinica.daos.PacienteDAO;
import com.mycompany.clinica.modelo.Cita;
import com.mycompany.clinica.modelo.Doctor;
import com.mycompany.clinica.modelo.Paciente;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CitaController implements Initializable {

    @FXML private ComboBox<Paciente> cmbPaciente;
    @FXML private ComboBox<Doctor> cmbDoctor;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Cita> tablaCitas;
    @FXML private TableColumn<Cita, Integer> colId;
    @FXML private TableColumn<Cita, Integer> colPaciente;
    @FXML private TableColumn<Cita, Integer> colDoctor;
    @FXML private TableColumn<Cita, LocalDate> colFecha;
    @FXML private TableColumn<Cita, String> colHora;
    @FXML private TableColumn<Cita, String> colEstado;

    private final CitaDAO citaDAO = new CitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarCombos();
        actualizarTabla();

        btnGuardar.setOnAction(e -> guardarCita());
        btnLimpiar.setOnAction(e -> limpiarCampos());
    }

    private void cargarCombos() {
        cmbPaciente.setItems(pacienteDAO.obtenerTodos());
        cmbDoctor.setItems(doctorDAO.obtenerTodos());
    }

    private void guardarCita() {
        if (cmbPaciente.getValue() == null || cmbDoctor.getValue() == null || 
            dpFecha.getValue() == null || txtHora.getText().trim().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Selecciona paciente, doctor, fecha y hora.", Alert.AlertType.WARNING);
            return;
        }

        Cita cita = new Cita();
        cita.setIdPaciente(cmbPaciente.getValue().getIdPaciente());
        cita.setIdDoctor(cmbDoctor.getValue().getIdDoctor());
        cita.setFecha(dpFecha.getValue());
        cita.setHora(txtHora.getText().trim());
        cita.setEstado("Pendiente");

        if (citaDAO.guardar(cita)) {
            actualizarTabla();
            limpiarCampos();
            mostrarAlerta("Cita Agendada", "La cita se registró correctamente en la base de datos.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo registrar la cita.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarTabla() {
        tablaCitas.setItems(citaDAO.obtenerTodos());
    }

    private void limpiarCampos() {
        cmbPaciente.setValue(null);
        cmbDoctor.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        tablaCitas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}