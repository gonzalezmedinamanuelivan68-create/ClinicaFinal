/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clinica.controlador;

import com.mycompany.clinica.modelo.AlmacenDatos;
import com.mycompany.clinica.modelo.Cita;
import com.mycompany.clinica.modelo.Doctor;
import com.mycompany.clinica.modelo.Paciente;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class CitaController implements Initializable {

  @FXML private ComboBox<Paciente> cmbPaciente;
    @FXML private ComboBox<Doctor> cmbDoctor;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private ComboBox<String> cmbEstado;

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Cita> tablaCitas;
    @FXML private TableColumn<Cita, Integer> colId;
    @FXML private TableColumn<Cita, Paciente> colPaciente;
    @FXML private TableColumn<Cita, Doctor> colDoctor;
    @FXML private TableColumn<Cita, String> colFecha;
    @FXML private TableColumn<Cita, String> colHora;
    @FXML private TableColumn<Cita, String> colEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Configurar las columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // 2. Llenar los ComboBox con los datos en memoria
        cmbPaciente.setItems(AlmacenDatos.getInstancia().getPacientes());
        cmbDoctor.setItems(AlmacenDatos.getInstancia().getDoctores());
        
        // 3. Llenar el ComboBox de estados con opciones estáticas
        cmbEstado.setItems(FXCollections.observableArrayList("Pendiente", "Completada", "Cancelada"));

        // 4. Conectar la tabla de citas
        tablaCitas.setItems(AlmacenDatos.getInstancia().getCitas());

        // 5. Asignar acciones a los botones
        btnGuardar.setOnAction(e -> guardarCita());
        btnLimpiar.setOnAction(e -> limpiarCampos());
        btnEliminar.setOnAction(e -> eliminarCita());
    }

    private void guardarCita() {
        Paciente pacienteSelec = cmbPaciente.getValue();
        Doctor doctorSelec = cmbDoctor.getValue();
        LocalDate fechaSeleccionada = dpFecha.getValue();
        String hora = txtHora.getText();
        String estado = cmbEstado.getValue();

        // Validar que no haya campos vacíos
        if (pacienteSelec == null || doctorSelec == null || fechaSeleccionada == null || hora.isEmpty() || estado == null) {
            mostrarAlerta("Campos incompletos", "Por favor, selecciona todos los datos requeridos para la cita.", Alert.AlertType.WARNING);
            return;
        }

        // Formatear la fecha a texto (YYYY-MM-DD)
        String fechaStr = fechaSeleccionada.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Crear la nueva cita y guardarla
        int id = AlmacenDatos.getInstancia().getSiguienteIdCita();
        Cita nuevaCita = new Cita(id, pacienteSelec, doctorSelec, fechaStr, hora, estado);

        AlmacenDatos.getInstancia().getCitas().add(nuevaCita);

        limpiarCampos();
        mostrarAlerta("Cita programada", "La cita se ha guardado exitosamente.", Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        cmbPaciente.getSelectionModel().clearSelection();
        cmbDoctor.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        txtHora.clear();
        cmbEstado.getSelectionModel().clearSelection();
    }

    private void eliminarCita() {
        Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            AlmacenDatos.getInstancia().getCitas().remove(seleccionada);
            mostrarAlerta("Cita eliminada", "La cita ha sido removida del sistema.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selección requerida", "Selecciona una cita en la tabla para eliminarla.", Alert.AlertType.WARNING);
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

