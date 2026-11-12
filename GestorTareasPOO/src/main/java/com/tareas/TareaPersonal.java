package com.tareas;
// Archivo: TareaPersonal.java
import java.time.LocalDate;

// Herencia: TareaPersonal extiende (hereda de) Tarea
public class TareaPersonal extends Tarea {

    private static final long serialVersionUID = 1L;

    private String categoria; // Atributo propio de esta clase hija

    // Constructor corto (sin fecha límite, prioridad media)
    public TareaPersonal(String titulo, String descripcion, String categoria) {
        super(titulo, descripcion); // super() llama al constructor de la clase padre
        this.categoria = categoria;
    }

    // Constructor completo
    public TareaPersonal(String titulo, String descripcion, String categoria, LocalDate fechaLimite, Prioridad prioridad) {
        super(titulo, descripcion, fechaLimite, prioridad);
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Polimorfismo: sobrescribimos los métodos abstractos de Tarea
    @Override
    public String getTipo() {
        return "Personal";
    }

    @Override
    public String getInfoExtra() {
        return "Categoría: " + categoria;
    }

    @Override
    public void mostrarDetalle() {
        String estado = isCompletada() ? "[Completada]" : "[Pendiente]";
        System.out.println(getTipo() + " " + estado + " - " + getTitulo() + " (" + getInfoExtra() + "): "
                + getDescripcion() + " | " + getTextoVencimiento());
    }
}
