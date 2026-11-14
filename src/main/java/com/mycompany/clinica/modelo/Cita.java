/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.modelo;

/**
 *
 * @author Usuario
 */
public class Cita {
    private int idCita;
    private Paciente paciente;
    private Doctor doctor;
    private String fecha; 
    private String hora; 
    private String estado; 

    public Cita() {
    }

    public Cita(int idCita, Paciente paciente, Doctor doctor, String fecha, String hora, String estado) {
        this.idCita = idCita;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public Paciente getIdPaciente() { return paciente; }
    public void setIdPaciente(Paciente paciente) { this.paciente = paciente; }

    public Doctor getIdDoctor() { return doctor; }
    public void setIdDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    @Override
    public String toString() {
    return "Cita: " + paciente.getNombre() + " - " + fecha + " " + hora;
    }
}

