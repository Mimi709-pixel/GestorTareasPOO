package com.tareas;
// Archivo: Prioridad.java
// Enum: un tipo con valores fijos. Evita usar textos sueltos como "alta" o "Alta".
public enum Prioridad {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    // Texto que se muestra en la página
    private final String etiqueta;

    Prioridad(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    // Convierte el texto del formulario en una Prioridad; si llega algo raro usa MEDIA
    public static Prioridad desdeTexto(String texto) {
        try {
            return Prioridad.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return MEDIA;
        }
    }
}
