package com.mycompany.clinica.modelo;

public class Doctor {
    private int idDoctor;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String cedula;
    private String rutaFoto; 

    public Doctor() {
    }

    public Doctor(int idDoctor, String nombre, String apellidos, String especialidad, String cedula) {
        this.idDoctor = idDoctor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.cedula = cedula;
    }

    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    
    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }
    
    @Override
    public String toString() {
        return "Dr. " + nombre + " " + apellidos + " (" + especialidad + ")";
    }
}

