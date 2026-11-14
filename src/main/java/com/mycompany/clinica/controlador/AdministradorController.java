package com.mycompany.clinica.controlador;

import com.mycompany.clinica.daos.AdministradorDAO;
import com.mycompany.clinica.modelos.Administrador;
import com.mycompany.clinica.modelos.EEstados;
import com.mycompany.clinica.modelos.ERoles;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class AdministradorController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtNumero;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtContraseña;
    @FXML private ComboBox<ERoles> cmbRol;
    @FXML private ComboBox<EEstados> cmbEstado;
    @FXML private TextField txtRutaImagen;
    @FXML private ImageView imgVistaPrevia;

    @FXML private TableView<Administrador> tblAdministradores;
    @FXML private TableColumn<Administrador, Integer> colId;
    @FXML private TableColumn<Administrador, String> colUsuario;
    @FXML private TableColumn<Administrador, String> colNombres;
    @FXML private TableColumn<Administrador, String> colCorreo;
    @FXML private TableColumn<Administrador, String> colNumero;
    @FXML private TableColumn<Administrador, String> colDireccion;
    @FXML private TableColumn<Administrador, ERoles> colRol;
    @FXML private TableColumn<Administrador, EEstados> colEstado;
    @FXML private TableColumn<Administrador, String> colRutaImagen;

    private final AdministradorDAO administradorDAO = new AdministradorDAO();
    private final ObservableList<Administrador> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Enlazar columnas con los atributos de la clase Administrador
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colRutaImagen.setCellValueFactory(new PropertyValueFactory<>("rutaImagen"));

        cmbRol.setItems(FXCollections.observableArrayList(ERoles.values()));
        cmbRol.setValue(ERoles.ADMINISTRADORES);

        cmbEstado.setItems(FXCollections.observableArrayList(EEstados.values()));
        cmbEstado.setValue(EEstados.ACTIVO);

        tblAdministradores.setItems(listaObservable);

        tblAdministradores.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> mostrarDetalleAdministrador(newValue)
        );

        actualizarTabla();
    }

    @FXML
    private void btnSeleccionarImagenOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto de Perfil");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File archivoSeleccionado = fileChooser.showOpenDialog(txtId.getScene().getWindow());
        if (archivoSeleccionado != null) {
            txtRutaImagen.setText(archivoSeleccionado.getAbsolutePath());
            cargarImagenVistaPrevia(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    private void btnGuardarOnAction(ActionEvent event) {
   
    Administrador admin = new Administrador();
    admin.setNombres(txtNombres.getText());
    admin.setNumero(txtNumero.getText());
    admin.setCorreo(txtCorreo.getText());
    admin.setDireccion(txtDireccion.getText());
    admin.setUsuario(txtUsuario.getText());
    admin.setContraseña(txtContraseña.getText()); 
    admin.setRol(cmbRol.getValue()); 
    admin.setEstado(cmbEstado.getValue());
    admin.setRutaImagen(txtRutaImagen.getText());

   
    AdministradorDAO dao = new AdministradorDAO();
    boolean exito = dao.guardar(admin);

    if (exito) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(null);
        alerta.setContentText("Guardado correctamente");
        alerta.showAndWait();
     
    } else {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText("No se pudo guardar. Revisa la conexión o campos duplicados (como el usuario).");
        alerta.showAndWait();
        }
    }

    @FXML
    private void btnActualizarOnAction (ActionEvent event) {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida", "Seleccione un administrador de la tabla.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String correo = txtCorreo.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String numero = txtNumero.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String contrasena = txtContraseña.getText().trim();
            ERoles rol = cmbRol.getValue();
            EEstados estado = cmbEstado.getValue();
            String rutaImagen = txtRutaImagen.getText().trim();

            Administrador adminActualizar = new Administrador(id, usuario, nombres, apellidos, correo, contrasena,numero, direccion, rol, rutaImagen, estado);

            if (administradorDAO.actualizar(adminActualizar)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro actualizado correctamente.");
                actualizarTabla();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la información.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Identificador no válido.");
        }
    }

    @FXML
    private void btnEliminarOnAction(ActionEvent event) {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida", "Seleccione un registro para eliminar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            if (administradorDAO.borrar(id)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Administrador eliminado correctamente.");
                actualizarTabla();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el registro.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Identificador no válido.");
        }
    }

    @FXML
    private void btnLimpiarOnAction(ActionEvent event) {
        limpiarCampos();
    }

    private void mostrarDetalleAdministrador(Administrador admin) {
        if (admin != null) {
            txtId.setText(String.valueOf(admin.getId()));
            txtNombres.setText(admin.getNombres());
            txtApellidos.setText(admin.getApellidos());
            txtCorreo.setText(admin.getCorreo());
            txtUsuario.setText(admin.getUsuario());
            txtNumero.setText(admin.getNumero());
            txtDireccion.setText(admin.getDireccion());
            txtContraseña.setText(admin.getContraseña());
            cmbRol.setValue(admin.getRol());
            cmbEstado.setValue(admin.getEstado());
            txtRutaImagen.setText(admin.getRutaImagen());
            cargarImagenVistaPrevia(admin.getRutaImagen());
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

    private void actualizarTabla() {
        listaObservable.setAll(administradorDAO.obtenerTodos());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCorreo.setText("");
        txtNumero.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtContraseña.setText("");
        cmbRol.setValue(ERoles.ADMINISTRADORES);
        cmbEstado.setValue(EEstados.ACTIVO);
        txtRutaImagen.setText("");
        imgVistaPrevia.setImage(null);
        tblAdministradores.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}