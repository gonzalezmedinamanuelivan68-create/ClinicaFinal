package com.mycompany.clinica.daos;

import com.mycompany.clinica.config.Conexion;
import com.mycompany.clinica.modelo.Consulta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConsultaDAO {

    public boolean guardar(Consulta consulta) {
        String sql = "INSERT INTO consultas (id_cita, diagnostico, tratamiento, notas) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, consulta.getIdCita());
                ps.setString(2, consulta.getDiagnostico());
                ps.setString(3, consulta.getTratamiento());
                ps.setString(4, consulta.getNotas());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar consulta: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Consulta> obtenerTodos() {
        ObservableList<Consulta> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM consultas";
        try (Connection con = Conexion.conectar()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consulta c = new Consulta();
                    c.setIdConsulta(rs.getInt("id_consulta"));
                    c.setIdCita(rs.getInt("id_cita"));
                    c.setDiagnostico(rs.getString("diagnostico"));
                    c.setTratamiento(rs.getString("tratamiento"));
                    c.setNotas(rs.getString("notas"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar consultas: " + e.getMessage());
        }
        return lista;
    }
}