package com.mycompany.clinica.config;

import com.mycompany.clinica.modelos.Administrador;

public class Sesion {
    private static Administrador usuarioActual;

    public static Administrador getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Administrador usuario) {
        usuarioActual = usuario;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}