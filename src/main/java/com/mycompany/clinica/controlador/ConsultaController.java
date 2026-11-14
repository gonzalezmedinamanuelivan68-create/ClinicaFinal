package com.mycompany.clinica.controlador;

import com.mycompany.clinica.daos.CitaDAO;
import com.mycompany.clinica.daos.ConsultaDAO;
import com.mycompany.clinica.modelo.Cita;
import com.mycompany.clinica.modelo.Consulta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConsultaController implements Initializable {

    @FXML private ComboBox<Cita> cmbCita;
    @FXML private TextArea txtDiagnostico;
    @FXML private TextArea txtTratamiento;
    @FXML private TextArea txtNotas;

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Consulta> tablaConsultas;
    @FXML private TableColumn<Consulta, Integer> colId;
    @FXML private TableColumn<Consulta, Integer> colCita;
    @FXML private TableColumn<Consulta, String> colDiagnostico;
    @FXML private TableColumn<Consulta, String> colTratamiento;
    @FXML private TableColumn<Consulta, String> colNotas;

    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idConsulta"));
        colCita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<>("tratamiento"));
        colNotas.setCellValueFactory(new PropertyValueFactory<>("notas"));

        cmbCita.setItems(citaDAO.obtenerTodos());
        actualizarTabla();

        btnGuardar.setOnAction(e -> guardarConsulta());
        btnLimpiar.setOnAction(e -> limpiarCampos());
    }

    private void guardarConsulta() {
        if (cmbCita.getValue() == null || txtDiagnostico.getText().trim().isEmpty() || 
            txtTratamiento.getText().trim().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Selecciona una cita y llena diagnóstico y tratamiento.", Alert.AlertType.WARNING);
            return;
        }

        Consulta consulta = new Consulta();
        consulta.setIdCita(cmbCita.getValue().getIdCita());
        consulta.setDiagnostico(txtDiagnostico.getText().trim());
        consulta.setTratamiento(txtTratamiento.getText().trim());
        consulta.setNotas(txtNotas.getText().trim());

        if (consultaDAO.guardar(consulta)) {
            actualizarTabla();
            limpiarCampos();
            mostrarAlerta("Consulta Guardada", "El historial médico se guardó correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar la consulta.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarTabla() {
        tablaConsultas.setItems(consultaDAO.obtenerTodos());
    }

    private void limpiarCampos() {
        cmbCita.setValue(null);
        txtDiagnostico.clear();
        txtTratamiento.clear();
        txtNotas.clear();
        tablaConsultas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}