/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clinica.controlador;

import com.mycompany.clinica.modelo.AlmacenDatos;
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
/**
 * FXML Controller class
 *
 * @author Gonzalez Medina Manuel Ivan
 */
public class ConsultaController implements Initializable {

 @FXML private ComboBox<Cita> cmbCita;
    @FXML private TextArea txtDiagnostico;
    @FXML private TextArea txtTratamiento;
    @FXML private TextArea txtNotas;

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Consulta> tablaConsultas;
    @FXML private TableColumn<Consulta, Integer> colId;
    @FXML private TableColumn<Consulta, Cita> colCita;
    @FXML private TableColumn<Consulta, String> colDiagnostico;
    @FXML private TableColumn<Consulta, String> colTratamiento;
    @FXML private TableColumn<Consulta, String> colNotas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idConsulta"));
        colCita.setCellValueFactory(new PropertyValueFactory<>("cita"));
        colDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<>("tratamiento"));
        colNotas.setCellValueFactory(new PropertyValueFactory<>("notas"));

        // 2. Llenar el ComboBox con las citas programadas en memoria
        cmbCita.setItems(AlmacenDatos.getInstancia().getCitas());

        // 3. Conectar la tabla de consultas
        tablaConsultas.setItems(AlmacenDatos.getInstancia().getConsultas());

        // 4. Asignar acciones a los botones
        btnGuardar.setOnAction(e -> guardarConsulta());
        btnLimpiar.setOnAction(e -> limpiarCampos());
        btnEliminar.setOnAction(e -> eliminarConsulta());
    }

    private void guardarConsulta() {
        Cita citaSelec = cmbCita.getValue();
        String diagnostico = txtDiagnostico.getText();
        String tratamiento = txtTratamiento.getText();
        String notas = txtNotas.getText();

        if (citaSelec == null || diagnostico.isEmpty() || tratamiento.isEmpty()) {
            mostrarAlerta("Campos incompletos", "Por favor, selecciona una cita y llena el diagnóstico y tratamiento.", Alert.AlertType.WARNING);
            return;
        }

        // Crear y guardar la consulta
        int id = AlmacenDatos.getInstancia().getSiguienteIdConsulta();
        Consulta nuevaConsulta = new Consulta(id, citaSelec, diagnostico, tratamiento, notas);

        AlmacenDatos.getInstancia().getConsultas().add(nuevaConsulta);

        // Opcional: Cambiar el estado de la cita a "Completada" automáticamente
        citaSelec.setEstado("Completada");
        // Refrescar la vista de citas si es necesario (TableView.refresh())

        limpiarCampos();
        mostrarAlerta("Consulta registrada", "Los datos de la consulta médica se han guardado exitosamente.", Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        cmbCita.getSelectionModel().clearSelection();
        txtDiagnostico.clear();
        txtTratamiento.clear();
        txtNotas.clear();
    }

    private void eliminarConsulta() {
        Consulta seleccionada = tablaConsultas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            AlmacenDatos.getInstancia().getConsultas().remove(seleccionada);
            mostrarAlerta("Consulta eliminada", "El registro de la consulta ha sido removido.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selección requerida", "Selecciona una consulta en la tabla para eliminarla.", Alert.AlertType.WARNING);
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
