package com.mycompany.clinica.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; 
    private static final String BASE_DATOS = "clinica_db";
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    
    private static final String URL = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + BASE_DATOS + "?useSSL=false&serverTimezone=UTC";

    public static Connection conectar() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos: " + BASE_DATOS);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Falló la conexión a la base de datos. Verifica que XAMPP esté encendido.");
            e.printStackTrace();
        }
        return con;
    }
    
    public static void desconectar(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }
}