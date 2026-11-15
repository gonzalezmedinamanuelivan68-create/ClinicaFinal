package com.mycompany.clinica.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.clinica.config.ApiCliente;
import com.mycompany.clinica.modelos.Administrador;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdministradorDAO {

    private final Gson gson = new Gson();
    
    public Administrador login(String usuario, String password) {
        try {
            Map<String, String> credenciales = new HashMap<>();
            credenciales.put("usuario", usuario);
            credenciales.put("password", password);

            String json = gson.toJson(credenciales);
            HttpResponse<String> response = ApiCliente.post("/login.php", json);

            // Líneas de diagnóstico en consola:
            System.out.println("STATUS HTTP: " + response.statusCode());
            System.out.println("RESPUESTA API: " + response.body());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), Administrador.class);
            }
        } catch (Exception e) {
            System.err.println("EXCEPCIÓN EN LOGIN: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



    public ObservableList<Administrador> obtenerTodos() {
        ObservableList<Administrador> lista = FXCollections.observableArrayList();
        try {
            HttpResponse<String> response = ApiCliente.get("/administradores.php");
            if (response.statusCode() == 200) {
                Type tipoLista = new TypeToken<List<Administrador>>() {}.getType();
                List<Administrador> adminsApi = gson.fromJson(response.body(), tipoLista);
                if (adminsApi != null) {
                    lista.addAll(adminsApi);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener administradores de la API: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Administrador admin) {
        try {
            String json = gson.toJson(admin);
            HttpResponse<String> response = ApiCliente.post("/administradores.php", json);
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Error al guardar administrador en API: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Administrador admin) {
        try {
            String json = gson.toJson(admin);
            HttpResponse<String> response = ApiCliente.put("/administradores.php?id=" + admin.getId(), json);
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error al actualizar administrador en API: " + e.getMessage());
            return false;
        }
    }

    public boolean borrar(int id) {
        try {
            HttpResponse<String> response = ApiCliente.delete("/administradores.php?id=" + id);
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Error al eliminar administrador en API: " + e.getMessage());
            return false;
        }
    }
}