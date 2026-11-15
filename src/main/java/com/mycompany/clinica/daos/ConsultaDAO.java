package com.mycompany.clinica.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.clinica.config.ApiCliente;
import com.mycompany.clinica.modelo.Consulta;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConsultaDAO {

    private final Gson gson = new Gson();

    public ObservableList<Consulta> obtenerTodos() {
        ObservableList<Consulta> lista = FXCollections.observableArrayList();
        try {
            HttpResponse<String> response = ApiCliente.get("/consultas.php");
            if (response.statusCode() == 200) {
                Type tipoLista = new TypeToken<List<Consulta>>() {}.getType();
                List<Consulta> consultasApi = gson.fromJson(response.body(), tipoLista);
                if (consultasApi != null) {
                    lista.addAll(consultasApi);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener consultas de la API: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Consulta consulta) {
        try {
            String json = gson.toJson(consulta);
            HttpResponse<String> response = ApiCliente.post("/consultas.php", json);
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Error al guardar consulta en API: " + e.getMessage());
            return false;
        }
    }
}