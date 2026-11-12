package com.tareas;
// Archivo: GestorTareas.java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// @Service: Spring crea un único objeto GestorTareas y se lo entrega al controlador.
// Esta clase contiene toda la lógica: agregar, completar, eliminar, filtrar y contar.
@Service
public class GestorTareas {

    // Asociación: el gestor almacena una lista de objetos tipo Tarea
    private List<Tarea> tareas;
    // Asociación: el gestor usa un ArchivoTareas para guardar (null = solo en memoria)
    private ArchivoTareas archivo;
    private int siguienteId = 1;

    // Constructor sin archivo: las tareas viven solo en memoria (lo usa Main, la versión de consola)
    public GestorTareas() {
        this.tareas = new ArrayList<>();
    }

    // Constructor con archivo: es el que usa Spring para la página web
    @Autowired
    public GestorTareas(ArchivoTareas archivo) {
        this.archivo = archivo;
        boolean primeraVez = !archivo.existe();
        this.tareas = archivo.cargar();
        for (Tarea t : tareas) {
            if (t.getId() >= siguienteId) {
                siguienteId = t.getId() + 1;
            }
        }
        if (primeraVez) {
            cargarEjemplos();
        }
    }

    // Tareas de ejemplo para que la app no arranque vacía la primera vez
    private void cargarEjemplos() {
        LocalDate hoy = LocalDate.now();
        agregarTarea(new TareaAcademica("Estudiar POO", "Repasar los 4 pilares para el parcial", "Programación II", hoy, Prioridad.ALTA));
        agregarTarea(new TareaLaboral("Enviar informe", "Informe semanal de avances", "Proyecto AulaFlow", hoy.plusDays(1), Prioridad.MEDIA));
        agregarTarea(new TareaPersonal("Comprar víveres", "Leche, fruta y café", "Hogar", hoy.plusDays(4), Prioridad.BAJA));
        agregarTarea(new TareaAcademica("Taller de cálculo", "Ejercicios de gradiente", "Cálculo multivariable", hoy.minusDays(2), Prioridad.ALTA));
    }

    // Guarda los cambios en el archivo (si hay uno)
    private void guardar() {
        if (archivo != null) {
            archivo.guardar(tareas);
        }
    }

    // ---------- Operaciones principales ----------

    // Recibe cualquier tipo de tarea gracias al polimorfismo
    public void agregarTarea(Tarea tarea) {
        tarea.setId(siguienteId++);
        tareas.add(tarea);
        guardar();
    }

    public Tarea buscarPorId(int id) {
        for (Tarea t : tareas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    // Cambia el estado: pendiente -> completada, o completada -> pendiente
    public boolean cambiarEstado(int id) {
        Tarea t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        t.setCompletada(!t.isCompletada());
        guardar();
        return true;
    }

    public boolean eliminarTarea(int id) {
        Tarea t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        tareas.remove(t);
        guardar();
        return true;
    }

    // ---------- Consultas ----------

    public List<Tarea> getTareas() {
        return tareas;
    }

    // Devuelve las tareas según el filtro, ordenadas:
    // primero las pendientes, luego por fecha límite más cercana y por prioridad
    public List<Tarea> filtrar(String filtro) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : tareas) {
            boolean incluir;
            switch (filtro) {
                case "pendientes":  incluir = !t.isCompletada(); break;
                case "completadas": incluir = t.isCompletada();  break;
                case "vencidas":    incluir = t.estaVencida();   break;
                default:            incluir = true;
            }
            if (incluir) {
                resultado.add(t);
            }
        }
        resultado.sort(Comparator
                .comparing(Tarea::isCompletada)
                .thenComparing(Tarea::getFechaLimite, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Tarea::getPrioridad));
        return resultado;
    }

    // Usa la interfaz Recordable: al gestor no le importa qué tipo de tarea es,
    // solo que sepa decir si necesita recordatorio
    public List<Recordable> getRecordatorios() {
        List<Recordable> avisos = new ArrayList<>();
        for (Tarea t : filtrar("pendientes")) {
            if (t.necesitaRecordatorio()) {
                avisos.add(t);
            }
        }
        return avisos;
    }

    // ---------- Estadísticas para el resumen ----------

    public int getTotal() {
        return tareas.size();
    }

    public int getCantidadCompletadas() {
        int cantidad = 0;
        for (Tarea t : tareas) {
            if (t.isCompletada()) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public int getCantidadPendientes() {
        return getTotal() - getCantidadCompletadas();
    }

    public int getCantidadVencidas() {
        int cantidad = 0;
        for (Tarea t : tareas) {
            if (t.estaVencida()) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public int getPorcentajeAvance() {
        if (getTotal() == 0) {
            return 0;
        }
        return getCantidadCompletadas() * 100 / getTotal();
    }

    // ---------- Versión consola ----------
    public void listarTareas() {
        System.out.println("\n========================================");
        System.out.println("          MIS DEBERES DIARIOS           ");
        System.out.println("========================================");

        for (Tarea t : tareas) {
            // POLIMORFISMO EN ACCIÓN:
            // Java decide en tiempo de ejecución qué mostrarDetalle() ejecutar
            // según si el objeto es TareaAcademica, TareaPersonal o TareaLaboral.
            t.mostrarDetalle();
        }
        System.out.println("========================================");
    }
}
