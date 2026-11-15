package com.mycompany.clinica.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.clinica.config.ApiCliente;
import com.mycompany.clinica.modelo.Paciente;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PacienteDAO {

    private final Gson gson = new Gson();

    public ObservableList<Paciente> obtenerTodos() {
        ObservableList<Paciente> lista = FXCollections.observableArrayList();
        try {
            HttpResponse<String> response = ApiCliente.get("/pacientes.php");
            if (response.statusCode() == 200) {
                Type tipoLista = new TypeToken<List<Paciente>>() {}.getType();
                List<Paciente> pacientesApi = gson.fromJson(response.body(), tipoLista);
                lista.addAll(pacientesApi);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener pacientes de la API: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Paciente p) {
        try {
            String json = gson.toJson(p);
            HttpResponse<String> response = ApiCliente.post("/pacientes.php", json);
            // 200 OK o 201 Created indican éxito
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Error al guardar paciente en API: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Paciente p) {
        try {
            String json = gson.toJson(p);
            HttpResponse<String> response = ApiCliente.put("/pacientes.php?id=" + p.getIdPaciente(), json);
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error al actualizar paciente en API: " + e.getMessage());
            return false;
        }
    }

    public boolean borrar(int idPaciente) {
        try {
            HttpResponse<String> response = ApiCliente.delete("/pacientes.php?id=" + idPaciente);
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error al eliminar paciente en API: " + e.getMessage());
            return false;
        }
    }
}