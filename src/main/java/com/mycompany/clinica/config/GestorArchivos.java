package com.mycompany.clinica.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GestorArchivos {

    // Ruta base: C:\Users\<Usuario>\Documents\ClinicaApp
    private static final String RUTA_DOCUMENTOS = System.getProperty("user.home") + File.separator + "Documents";
    private static final Path CARPETA_PRINCIPAL = Paths.get(RUTA_DOCUMENTOS, "ClinicaApp");
    private static final Path CARPETA_IMAGENES = CARPETA_PRINCIPAL.resolve("imagenes");
    private static final Path ARCHIVO_LOG = CARPETA_PRINCIPAL.resolve("registro_directorios.log");

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Crea la carpeta principal y la subcarpeta de imágenes si no existen,
     * registrando la operación en el archivo log.
     */
    public static void inicializarDirectorios() {
        try {
            if (!Files.exists(CARPETA_PRINCIPAL)) {
                Files.createDirectories(CARPETA_PRINCIPAL);
                escribirLog("Creación de carpeta principal: " + CARPETA_PRINCIPAL, "EXITOSO");
            }

            if (!Files.exists(CARPETA_IMAGENES)) {
                Files.createDirectories(CARPETA_IMAGENES);
                escribirLog("Creación de subcarpeta de imágenes: " + CARPETA_IMAGENES, "EXITOSO");
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorios externos: " + e.getMessage());
            escribirLog("Error al crear directorios (" + e.getMessage() + ")", "FALLIDO");
        }
    }

    /**
     * Copia una imagen seleccionada a Documentos/ClinicaApp/imagenes y devuelve la nueva ruta.
     */
    public static String guardarImagen(String rutaOrigen) {
        if (rutaOrigen == null || rutaOrigen.trim().isEmpty()) {
            return "";
        }

        inicializarDirectorios();

        File archivoOrigen = new File(rutaOrigen);
        if (!archivoOrigen.exists()) {
            return "";
        }

        
        if (archivoOrigen.toPath().toAbsolutePath().startsWith(CARPETA_IMAGENES.toAbsolutePath())) {
            return archivoOrigen.getAbsolutePath();
        }

        try {
            String nombreOriginal = archivoOrigen.getName();
            String extension = "";
            int punto = nombreOriginal.lastIndexOf('.');
            if (punto > 0) {
                extension = nombreOriginal.substring(punto);
            }

            
            String nuevoNombre = "img_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            Path destino = CARPETA_IMAGENES.resolve(nuevoNombre);

            Files.copy(archivoOrigen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return destino.toAbsolutePath().toString();

        } catch (IOException e) {
            System.err.println("Error al copiar imagen a Documentos: " + e.getMessage());
            return "";
        }
    }

    /**
     * Elimina físicamente una imagen de Documentos/ClinicaApp/imagenes.
     */
    public static void eliminarImagen(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            return;
        }
        try {
            Path ruta = Paths.get(rutaImagen);
            if (ruta.toAbsolutePath().startsWith(CARPETA_IMAGENES.toAbsolutePath())) {
                Files.deleteIfExists(ruta);
            }
        } catch (IOException e) {
            System.err.println("No se pudo eliminar el archivo de imagen: " + e.getMessage());
        }
    }

    private static void escribirLog(String operacion, String resultado) {
        try {
            if (!Files.exists(CARPETA_PRINCIPAL)) {
                Files.createDirectories(CARPETA_PRINCIPAL);
            }
            String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
            String linea = String.format("[%s] | OPERACIÓN: %s | RESULTADO: %s%n", fechaHora, operacion, resultado);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_LOG.toFile(), true))) {
                writer.write(linea);
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el archivo log: " + e.getMessage());
        }
    }
}