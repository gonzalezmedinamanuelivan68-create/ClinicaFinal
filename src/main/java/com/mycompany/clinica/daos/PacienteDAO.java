package com.mycompany.clinica.daos;

import com.mycompany.clinica.config.Conexion;
import com.mycompany.clinica.modelo.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PacienteDAO {

    public boolean guardar(Paciente p) {
        String sql = "INSERT INTO pacientes (nombre, apellidos, edad, telefono, ruta_foto) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getApellidos());
                ps.setInt(3, p.getEdad());
                ps.setString(4, p.getTelefono());
                ps.setString(5, p.getRutaFoto());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar paciente: " + e.getMessage());
            return false;
        }
    }

    public boolean borrar(int idPaciente) {
        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idPaciente);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            return false;
        }
    }
    public boolean actualizar(Paciente p) {
        String sql = "UPDATE pacientes SET nombre = ?, apellidos = ?, edad = ?, telefono = ?, ruta_foto = ? WHERE id_paciente = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getApellidos());
                ps.setInt(3, p.getEdad());
                ps.setString(4, p.getTelefono());
                ps.setString(5, p.getRutaFoto());
                ps.setInt(6, p.getIdPaciente());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Paciente> obtenerTodos() {
        ObservableList<Paciente> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM pacientes";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paciente p = new Paciente();
                    p.setIdPaciente(rs.getInt("id_paciente")); 
                    p.setNombre(rs.getString("nombre"));
                    p.setApellidos(rs.getString("apellidos"));
                    p.setEdad(rs.getInt("edad"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setRutaFoto(rs.getString("ruta_foto"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }
        return lista;
    }
}