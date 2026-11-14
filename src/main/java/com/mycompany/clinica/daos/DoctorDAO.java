package com.mycompany.clinica.daos;

import com.mycompany.clinica.config.Conexion;
import com.mycompany.clinica.modelo.Doctor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DoctorDAO {

    public boolean guardar(Doctor doc) {
        String sql = "INSERT INTO doctores (nombre, apellidos, especialidad, cedula, ruta_foto) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, doc.getNombre());
                ps.setString(2, doc.getApellidos());
                ps.setString(3, doc.getEspecialidad());
                ps.setString(4, doc.getCedula());
                ps.setString(5, doc.getRutaFoto());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar doctor: " + e.getMessage());
            return false;
        }
    }

    public boolean borrar(int idDoctor) {
        String sql = "DELETE FROM doctores WHERE id_doctor = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idDoctor);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar doctor: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Doctor> obtenerTodos() {
        ObservableList<Doctor> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM doctores";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor d = new Doctor();
                    d.setIdDoctor(rs.getInt("id_doctor"));
                    d.setNombre(rs.getString("nombre"));
                    d.setApellidos(rs.getString("apellidos"));
                    d.setEspecialidad(rs.getString("especialidad"));
                    d.setCedula(rs.getString("cedula"));
                    d.setRutaFoto(rs.getString("ruta_foto"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar doctores: " + e.getMessage());
        }
        return lista;
    }
}
