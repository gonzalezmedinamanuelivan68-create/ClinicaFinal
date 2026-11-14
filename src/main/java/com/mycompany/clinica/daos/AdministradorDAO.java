package com.mycompany.clinica.daos;

import com.mycompany.clinica.modelos.Administrador;
import com.mycompany.clinica.config.Conexion;
import com.mycompany.clinica.modelos.EEstados;
import com.mycompany.clinica.modelos.ERoles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {
    public Administrador login(String usuario, String password) {
        String sql = "SELECT * FROM administradores WHERE usuario = ? AND password = ? AND estado = 'ACTIVO'";
        
        try (Connection con = Conexion.conectar()) {
            if (con == null) return null;
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, usuario);
                ps.setString(2, password);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Administrador admin = new Administrador();
                        admin.setId(rs.getInt("id"));
                        admin.setNombres(rs.getString("nombre"));
                        admin.setUsuario(rs.getString("usuario"));
                        admin.setRol(ERoles.valueOf(rs.getString("rol").toUpperCase()));
                        admin.setEstado(EEstados.valueOf(rs.getString("estado").toUpperCase()));
                        return admin;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return null;
    }

    public boolean guardar(Administrador admin) {
        String sql = "INSERT INTO administradores (nombre, telefono, email, direccion, usuario, password, rol, estado, ruta_foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection con = Conexion.conectar();
        PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, admin.getNombres());
        ps.setString(2, admin.getNumero());
        ps.setString(3, admin.getCorreo());
        ps.setString(4, admin.getDireccion());
        ps.setString(5, admin.getUsuario());
        ps.setString(6, admin.getContraseña());
        ps.setString(7, admin.getRol().name());
        ps.setString(8, admin.getEstado().name());
        ps.setString(9, admin.getRutaImagen());
        
        return ps.executeUpdate() > 0;
        
    } catch (SQLException e) {
        System.err.println("Error al guardar administrador: " + e.getMessage());
        return false;
    }
    }

    public boolean actualizar(Administrador admin) {
        Connection con = null;
     
        String sql = "UPDATE administradores SET usuario=?, nombres=?, apellidos=?, correo=?, contraseña=?, rol=?, ruta_imagen=?, estados=? WHERE id=?;";
        try {
            con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, admin.getUsuario());
            ps.setString(2, admin.getNombres());
            ps.setString(3, admin.getApellidos());
            ps.setString(4, admin.getCorreo());
            ps.setString(5, admin.getContraseña());
            ps.setString(6, admin.getRol().name());
            ps.setString(7, admin.getRutaImagen());
            ps.setString(8, admin.getEstado().name());
            ps.setInt(9, admin.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR Actualizar: " + e.getMessage());
        }
        return false;
    }

    public boolean borrar(int id) {
        Connection con = null;
      
        String sql = "DELETE FROM administradores WHERE id=?;";
        try {
            con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR Borrar: " + e.getMessage());
        }
        return false;
    }

    public List<Administrador> obtenerTodos() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM administradores";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Administrador admin = new Administrador();
                
                admin.setId(rs.getInt("id"));
                admin.setNombres(rs.getString("nombre")); 
                admin.setNumero(rs.getString("telefono")); 
                admin.setCorreo(rs.getString("email"));
                admin.setDireccion(rs.getString("direccion"));
                admin.setUsuario(rs.getString("usuario"));
                admin.setContraseña(rs.getString("password"));
                admin.setRutaImagen(rs.getString("ruta_foto"));
                
                
                try {
                    
                    String rolBD = rs.getString("rol");
                    if (rolBD != null) admin.setRol(ERoles.valueOf(rolBD.toUpperCase()));
                } catch (Exception e) {
                    System.err.println("Rol no reconocido en BD: " + rs.getString("rol"));
                    
                    admin.setRol(ERoles.ADMINISTRADOR); 
                }
                
                try {
                    String estadoBD = rs.getString("estado");
                    if (estadoBD != null) admin.setEstado(EEstados.valueOf(estadoBD.toUpperCase()));
                } catch (Exception e) {
                    System.err.println("Estado no reconocido en BD: " + rs.getString("estado"));
                    admin.setEstado(EEstados.ACTIVO); // Valor por defecto
                }
                
                lista.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener Todos: " + e.getMessage());
        }
        
        return lista;
    }
    
}