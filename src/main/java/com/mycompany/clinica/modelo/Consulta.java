/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinica.modelo;

/**
 *
 * @author Usuario
 */
public class Consulta {
    private int idConsulta;
    private Cita idcita;
    private String diagnostico;
    private String tratamiento;
    private String notas;

    public Consulta() {
    }

    public Consulta(int idConsulta, Cita idcita, String diagnostico, String tratamiento, String notas) {
        this.idConsulta = idConsulta;
        this.idcita = idcita;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.notas = notas;
    }

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public Cita getIdCita() { return idcita; }
    public void setIdCita(Cita idcita) { this.idcita = idcita; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
