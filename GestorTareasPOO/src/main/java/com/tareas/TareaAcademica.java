package com.tareas;
// Archivo: TareaAcademica.java
import java.time.LocalDate;

// Herencia: TareaAcademica extiende (hereda de) Tarea
public class TareaAcademica extends Tarea {

    private static final long serialVersionUID = 1L;

    private String materia; // Atributo propio de esta clase hija

    // Constructor corto (sin fecha límite, prioridad media)
    public TareaAcademica(String titulo, String descripcion, String materia) {
        super(titulo, descripcion); // super() llama al constructor de la clase padre
        this.materia = materia;
    }

    // Constructor completo
    public TareaAcademica(String titulo, String descripcion, String materia, LocalDate fechaLimite, Prioridad prioridad) {
        super(titulo, descripcion, fechaLimite, prioridad);
        this.materia = materia;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    // Polimorfismo: sobrescribimos los métodos abstractos de Tarea
    @Override
    public String getTipo() {
        return "Académica";
    }

    @Override
    public String getInfoExtra() {
        return "Materia: " + materia;
    }

    @Override
    public void mostrarDetalle() {
        String estado = isCompletada() ? "[Completada]" : "[Pendiente]";
        System.out.println(getTipo() + " " + estado + " - " + getTitulo() + " (" + getInfoExtra() + "): "
                + getDescripcion() + " | " + getTextoVencimiento());
    }
}
