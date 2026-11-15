package com.mycompany.clinica.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.clinica.config.ApiCliente;
import com.mycompany.clinica.modelo.Cita;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CitaDAO {

    private final Gson gson = new Gson();

    public ObservableList<Cita> obtenerTodos() {
        ObservableList<Cita> lista = FXCollections.observableArrayList();
        try {
            HttpResponse<String> response = ApiCliente.get("/citas.php");
            if (response.statusCode() == 200) {
                Type tipoLista = new TypeToken<List<Cita>>() {}.getType();
                List<Cita> citasApi = gson.fromJson(response.body(), tipoLista);
                if (citasApi != null) {
                    lista.addAll(citasApi);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener citas de la API: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Cita cita) {
        try {
            String json = gson.toJson(cita);
            HttpResponse<String> response = ApiCliente.post("/citas.php", json);
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Error al guardar cita en API: " + e.getMessage());
            return false;
        }
    }
}