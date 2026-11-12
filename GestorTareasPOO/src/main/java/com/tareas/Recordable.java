package com.tareas;
// Archivo: Recordable.java
// Interfaz: define un "contrato". Toda clase que la implemente
// se compromete a saber avisar cuándo necesita un recordatorio.
public interface Recordable {

    // Indica si hay que mostrar un aviso (está vencida o por vencer)
    boolean necesitaRecordatorio();

    // Texto del aviso que se muestra en el panel de recordatorios
    String getMensajeRecordatorio();
}
