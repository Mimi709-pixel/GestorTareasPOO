package com.tareas;
// Archivo: ArchivoTareas.java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

// Responsabilidad única: esta clase solo sabe guardar y leer tareas de un archivo.
// Así las tareas no se pierden al cerrar la aplicación.
@Component
public class ArchivoTareas {

    private final String ruta;

    // Por defecto guarda en "tareas.dat", dentro de la carpeta del proyecto
    public ArchivoTareas() {
        this("tareas.dat");
    }

    public ArchivoTareas(String ruta) {
        this.ruta = ruta;
    }

    public boolean existe() {
        return new File(ruta).exists();
    }

    // Serialización: escribe la lista completa de objetos en el archivo
    public void guardar(List<Tarea> tareas) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(new ArrayList<>(tareas));
        } catch (IOException e) {
            System.out.println("No se pudieron guardar las tareas: " + e.getMessage());
        }
    }

    // Lee la lista del archivo. Si no existe o está dañado, devuelve una lista vacía.
    @SuppressWarnings("unchecked")
    public List<Tarea> cargar() {
        if (!existe()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<Tarea>) entrada.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.out.println("No se pudieron leer las tareas guardadas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
