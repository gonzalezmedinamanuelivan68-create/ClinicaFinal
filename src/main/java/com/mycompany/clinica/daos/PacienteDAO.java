package com.mycompany.clinica.daos;

import com.mycompany.clinica.modelo.Paciente;
import com.mycompany.clinica.config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PacienteDAO {

    public boolean guardar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (nombre, apellidos, edad, telefono, ruta_foto) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, paciente.getNombre());
                ps.setString(2, paciente.getApellidos());
                ps.setInt(3, paciente.getEdad());
                ps.setString(4, paciente.getTelefono());
                ps.setString(5, paciente.getRutaFoto());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar paciente: " + e.getMessage());
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
            System.err.println("Error al obtener pacientes: " + e.getMessage());
        }
        return lista;
    }
    
    // Aquí agregarías después tus métodos actualizar(Paciente p) y borrar(int id)
}
