package com.tareas;
// Archivo: TareaLaboral.java
import java.time.LocalDate;

// Herencia: TareaLaboral extiende (hereda de) Tarea
public class TareaLaboral extends Tarea {

    private static final long serialVersionUID = 1L;

    private String proyecto; // Atributo propio de esta clase hija

    // Constructor corto (sin fecha límite, prioridad media)
    public TareaLaboral(String titulo, String descripcion, String proyecto) {
        super(titulo, descripcion); // super() llama al constructor de la clase padre
        this.proyecto = proyecto;
    }

    // Constructor completo
    public TareaLaboral(String titulo, String descripcion, String proyecto, LocalDate fechaLimite, Prioridad prioridad) {
        super(titulo, descripcion, fechaLimite, prioridad);
        this.proyecto = proyecto;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    // Polimorfismo: sobrescribimos los métodos abstractos de Tarea
    @Override
    public String getTipo() {
        return "Laboral";
    }

    @Override
    public String getInfoExtra() {
        return "Proyecto: " + proyecto;
    }

    @Override
    public void mostrarDetalle() {
        String estado = isCompletada() ? "[Completada]" : "[Pendiente]";
        System.out.println(getTipo() + " " + estado + " - " + getTitulo() + " (" + getInfoExtra() + "): "
                + getDescripcion() + " | " + getTextoVencimiento());
    }
}
