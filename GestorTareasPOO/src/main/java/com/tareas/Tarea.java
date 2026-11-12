package com.tareas;
// Archivo: Tarea.java
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Abstracción: clase abstracta que sirve como plantilla general de cualquier tarea.
// Implementa Recordable (sabe avisar cuándo vence) y Serializable (se puede guardar en un archivo).
public abstract class Tarea implements Recordable, Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Encapsulamiento: atributos privados (ocultos al exterior)
    private int id;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private LocalDate fechaLimite; // puede ser null si la tarea no tiene fecha
    private Prioridad prioridad;

    // Constructor corto: tarea sin fecha límite y con prioridad media
    // (Sobrecarga: dos constructores con el mismo nombre y distintos parámetros)
    public Tarea(String titulo, String descripcion) {
        this(titulo, descripcion, null, Prioridad.MEDIA);
    }

    // Constructor completo
    public Tarea(String titulo, String descripcion, LocalDate fechaLimite, Prioridad prioridad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.prioridad = (prioridad == null) ? Prioridad.MEDIA : prioridad;
        this.completada = false; // Toda tarea nace pendiente por defecto
    }

    // ---------- Getters y Setters (acceso controlado a los atributos privados) ----------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    // ---------- Comportamiento común a todas las tareas ----------
    public void marcarComoCompletada() {
        this.completada = true;
    }

    public boolean tieneFecha() {
        return fechaLimite != null;
    }

    // Días que faltan para la fecha límite (negativo si ya pasó)
    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaLimite);
    }

    // Una tarea está vencida si sigue pendiente y su fecha ya pasó
    public boolean estaVencida() {
        return !completada && tieneFecha() && getDiasRestantes() < 0;
    }

    // Texto corto para mostrar junto a la tarea
    public String getTextoVencimiento() {
        if (!tieneFecha()) {
            return "Sin fecha límite";
        }
        String fecha = fechaLimite.format(FORMATO_FECHA);
        if (completada) {
            return "Fecha límite: " + fecha;
        }
        long dias = getDiasRestantes();
        if (dias < -1) {
            return "Vencida hace " + (-dias) + " días (" + fecha + ")";
        } else if (dias == -1) {
            return "Vencida desde ayer (" + fecha + ")";
        } else if (dias == 0) {
            return "Vence hoy";
        } else if (dias == 1) {
            return "Vence mañana";
        }
        return "Vence en " + dias + " días (" + fecha + ")";
    }

    // ---------- Implementación de la interfaz Recordable ----------
    // Se avisa cuando la tarea sigue pendiente y vence mañana, hoy o ya venció
    @Override
    public boolean necesitaRecordatorio() {
        return !completada && tieneFecha() && getDiasRestantes() <= 1;
    }

    @Override
    public String getMensajeRecordatorio() {
        // getTipo() y getInfoExtra() los responde la clase hija (Polimorfismo)
        return getTitulo() + " · " + getTipo() + " (" + getInfoExtra() + ") · " + getTextoVencimiento();
    }

    // Nombre de la clase real del objeto (TareaAcademica, TareaPersonal...).
    // La página lo usa para darle un color distinto a cada tipo.
    public String getNombreClase() {
        return getClass().getSimpleName();
    }

    // ---------- Métodos abstractos: cada clase hija los implementa a su manera ----------
    public abstract String getTipo();

    public abstract String getInfoExtra();

    public abstract void mostrarDetalle();
}
