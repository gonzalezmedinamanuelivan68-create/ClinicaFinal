package com.mycompany.clinica.modelos;

public class Administrador {
    private int id;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String contraseña;
    private String numero;
    private String direccion;
    private ERoles rol;
    private String rutaImagen;
    private EEstados estado;

  

    public Administrador(int id, String usuario, String nombres, String apellidos, String correo, String contraseña, String numero, String direccion, ERoles rol, String rutaImagen, EEstados estado) {
        this.id = id;
        this.usuario = usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contraseña = contraseña;
        this.rol = rol;
        this.direccion = direccion;
        this.numero = numero;
        this.rutaImagen = rutaImagen;
        this.estado = estado;
    }

    public Administrador() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public ERoles getRol() { return rol; }
    public void setRol(ERoles rol) { this.rol = rol; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }

    public EEstados getEstado() { return estado; }
    public void setEstado(EEstados estado) { this.estado = estado; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return "Administrador{" + "id=" + id + ", usuario=" + usuario + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo=" + correo + ", rol=" + rol + ", estado=" + estado + 
                ", numero=" + numero + ", direccion=" + direccion +'}';
    }
}