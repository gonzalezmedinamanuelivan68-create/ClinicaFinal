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
    private Paciente idpaciente;
    private Doctor iddoctor;
    private String fecha; 
    private String hora; 
    private String estado; 

    public Cita() {
    }

    public Cita(int idCita, Paciente paciente, Doctor doctor, String fecha, String hora, String estado) {
        this.idCita = idCita;
        this.idpaciente = paciente;
        this.iddoctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public Paciente getIdPaciente() { return idpaciente; }
    public void setIdPaciente(Paciente idpaciente) { this.idpaciente = idpaciente; }

    public Doctor getIdDoctor() { return iddoctor; }
    public void setIdDoctor(Doctor iddoctor) { this.iddoctor = iddoctor; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    @Override
    public String toString() {
    return "Cita: " + idpaciente.getNombre() + iddoctor.getNombre()+ " - " + fecha + " " + hora;
    }
}

