package com.tareas;
// Archivo: Main.java
// Versión de CONSOLA (demostración). La página web se ejecuta desde TareaApplication.
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciamos el gestor (sin archivo: solo en memoria)
        GestorTareas miGestor = new GestorTareas();

        // 2. Creamos tareas usando polimorfismo (variable tipo Tarea, objeto tipo hija)
        Tarea tarea1 = new TareaAcademica("Estudiar POO", "Repasar los 4 pilares", "Programación II",
                LocalDate.now(), Prioridad.ALTA);
        Tarea tarea2 = new TareaPersonal("Comprar víveres", "Leche, fruta y café", "Hogar");
        Tarea tarea3 = new TareaLaboral("Enviar informe", "Informe semanal", "Proyecto AulaFlow",
                LocalDate.now().minusDays(1), Prioridad.MEDIA);

        // 3. Modificamos el estado de una tarea (Encapsulamiento)
        tarea2.marcarComoCompletada();

        // 4. Agregamos las tareas al gestor
        miGestor.agregarTarea(tarea1);
        miGestor.agregarTarea(tarea2);
        miGestor.agregarTarea(tarea3);

        // 5. Reporte final
        miGestor.listarTareas();

        // 6. Recordatorios usando la interfaz Recordable
        System.out.println("RECORDATORIOS:");
        for (Recordable r : miGestor.getRecordatorios()) {
            System.out.println(" - " + r.getMensajeRecordatorio());
        }
    }
}
