/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.modelo;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;

/**
 *
 * @author Usuario
 */
/*
public class AlmacenDatos {
    
    private static AlmacenDatos instancia;
    
    private final ObservableList<Paciente> pacientes;
    private final ObservableList<Doctor> doctores;
    private final ObservableList<Cita> citas;
    private final ObservableList<Consulta> consultas;

    private int contadorPacientes = 1;
    private int contadorDoctores = 1;
    private int contadorCitas = 1;
    private int contadorConsultas = 1;

    private AlmacenDatos() {
        pacientes = FXCollections.observableArrayList();
        doctores = FXCollections.observableArrayList();
        citas = FXCollections.observableArrayList();
        consultas = FXCollections.observableArrayList();
    }

    public static AlmacenDatos getInstancia() {
        if (instancia == null) {
            instancia = new AlmacenDatos();
        }
        return instancia;
    }


    public ObservableList<Paciente> getPacientes() { return pacientes; }
    public ObservableList<Doctor> getDoctores() { return doctores; }
    public ObservableList<Cita> getCitas() { return citas; }
    public ObservableList<Consulta> getConsultas() { return consultas; }
    
    public int getSiguienteIdPaciente() {
        return contadorPacientes++;
    }
    public int getSiguienteIdDoctor() {
        return contadorDoctores++;
    }
    public int getSiguienteIdCita() {
        return contadorCitas++;
    }
    public int getSiguienteIdConsulta() {
        return contadorConsultas++;
    }
}

