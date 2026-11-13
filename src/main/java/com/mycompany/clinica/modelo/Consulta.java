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
    private Cita cita;
    private String diagnostico;
    private String tratamiento;
    private String notas;

    public Consulta() {
    }

    public Consulta(int idConsulta, Cita cita, String diagnostico, String tratamiento, String notas) {
        this.idConsulta = idConsulta;
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.notas = notas;
    }

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
