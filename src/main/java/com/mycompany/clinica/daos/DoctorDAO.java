package com.mycompany.clinica.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.clinica.config.ApiCliente;
import com.mycompany.clinica.modelo.Doctor;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DoctorDAO {

    private final Gson gson = new Gson();

    public ObservableList<Doctor> obtenerTodos() {
        ObservableList<Doctor> lista = FXCollections.observableArrayList();
        try {
            HttpResponse<String> response = ApiCliente.get("/doctores.php");
            if (response.statusCode() == 200) {
                Type tipoLista = new TypeToken<List<Doctor>>() {}.getType();
                List<Doctor> doctoresApi = gson.fromJson(response.body(), tipoLista);
                if (doctoresApi != null) {
                    lista.addAll(doctoresApi);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener doctores de la API: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Doctor doc) {
        try {
            String json = gson.toJson(doc);
            HttpResponse<String> response = ApiCliente.post("/doctores.php", json);
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Error al guardar doctor en API: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Doctor doc) {
        try {
            String json = gson.toJson(doc);
            HttpResponse<String> response = ApiCliente.put("/doctores.php?id=" + doc.getIdDoctor(), json);
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error al actualizar doctor en API: " + e.getMessage());
            return false;
        }
    }

    public boolean borrar(int idDoctor) {
        try {
            HttpResponse<String> response = ApiCliente.delete("/doctores.php?id=" + idDoctor);
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error al eliminar doctor en API: " + e.getMessage());
            return false;
        }
    }
}