package com.mycompany.clinica.daos;

import com.mycompany.clinica.config.Conexion;
import com.mycompany.clinica.modelo.Cita;
import com.mycompany.clinica.modelo.Doctor;
import com.mycompany.clinica.modelo.Paciente;
import java.sql.Connection;
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
                ps.setInt(1, cita.getIdPaciente().getIdPaciente());
                ps.setInt(2, cita.getIdDoctor().getIdDoctor());
                ps.setString(3, cita.getFecha());
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
        // Hacemos JOIN para traer también los nombres de paciente y doctor para la tabla y ComboBox
        String sql = "SELECT c.*, p.nombre AS p_nom, p.apellidos AS p_ape, d.nombre AS d_nom, d.apellidos AS d_ape, d.especialidad " +
                     "FROM citas c " +
                     "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN doctores d ON c.id_doctor = d.id_doctor";
                     
        try (Connection con = Conexion.conectar()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paciente p = new Paciente();
                    p.setIdPaciente(rs.getInt("id_paciente"));
                    p.setNombre(rs.getString("p_nom"));
                    p.setApellidos(rs.getString("p_ape"));

                    Doctor d = new Doctor();
                    d.setIdDoctor(rs.getInt("id_doctor"));
                    d.setNombre(rs.getString("d_nom"));
                    d.setApellidos(rs.getString("d_ape"));
                    d.setEspecialidad(rs.getString("especialidad"));

                    Cita c = new Cita();
                    c.setIdCita(rs.getInt("id_cita"));
                    c.setIdPaciente(p);
                    c.setIdDoctor(d);
                    c.setFecha(rs.getString("fecha"));
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