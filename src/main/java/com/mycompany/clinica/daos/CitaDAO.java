package com.mycompany.clinica.daos;

import com.mycompany.clinica.config.Conexion;
import com.mycompany.clinica.modelo.Cita;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CitaDAO {

    public boolean guardar(Cita cita) {
        String sql = "INSERT INTO citas (id_paciente, id_doctor, fecha, hora, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cita.getIdPaciente());
                ps.setInt(2, cita.getIdDoctor());
                ps.setDate(3, Date.valueOf(cita.getFecha())); // Convierte LocalDate a SQL Date
                ps.setString(4, cita.getHora());
                ps.setString(5, cita.getEstado() != null ? cita.getEstado() : "Pendiente");
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar cita: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Cita> obtenerTodos() {
        ObservableList<Cita> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM citas";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    c.setIdPaciente(rs.getInt("id_paciente"));
                    c.setIdDoctor(rs.getInt("id_doctor"));
                    c.setFecha(rs.getDate("fecha").toLocalDate());
                    c.setHora(rs.getString("hora"));
                    c.setEstado(rs.getString("estado"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }
}
