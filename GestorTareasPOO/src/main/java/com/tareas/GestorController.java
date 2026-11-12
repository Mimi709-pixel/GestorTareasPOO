package com.tareas;
// Archivo: GestorController.java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// El controlador solo recibe lo que llega de la página y se lo pasa al gestor.
// La lógica vive en GestorTareas y en las clases Tarea.
@Controller
public class GestorController {

    // Asociación: el controlador usa un GestorTareas
    private final GestorTareas gestor;

    // Spring entrega el GestorTareas por el constructor (inyección de dependencias)
    public GestorController(GestorTareas gestor) {
        this.gestor = gestor;
    }

    // Página principal. El filtro llega en la dirección: /?filtro=pendientes
    @GetMapping("/")
    public String verInicio(@RequestParam(name = "filtro", defaultValue = "todas") String filtro, Model model) {
        model.addAttribute("tareas", gestor.filtrar(filtro));
        model.addAttribute("recordatorios", gestor.getRecordatorios());
        model.addAttribute("gestor", gestor);
        model.addAttribute("filtro", filtro);
        model.addAttribute("prioridades", Prioridad.values());
        model.addAttribute("hoy", LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale.of("es", "CO"))));
        return "index";
    }

    @PostMapping("/agregar")
    public String agregarTarea(
            @RequestParam("titulo") String titulo,
            @RequestParam(name = "descripcion", defaultValue = "") String descripcion,
            @RequestParam("tipo") String tipo,
            @RequestParam(name = "extra", defaultValue = "") String extra,
            @RequestParam(name = "fecha", defaultValue = "") String fecha,
            @RequestParam(name = "prioridad", defaultValue = "MEDIA") String prioridad,
            RedirectAttributes mensajes) {

        // Validación: no se aceptan tareas sin título
        if (titulo.isBlank()) {
            mensajes.addFlashAttribute("error", "El título no puede estar vacío.");
            return "redirect:/";
        }

        Tarea nueva = crearTarea(tipo, titulo.trim(), descripcion.trim(), extra.trim(),
                convertirFecha(fecha), Prioridad.desdeTexto(prioridad));
        gestor.agregarTarea(nueva);

        mensajes.addFlashAttribute("mensaje", "Tarea \"" + nueva.getTitulo() + "\" agregada.");
        return "redirect:/";
    }

    @PostMapping("/completar")
    public String cambiarEstado(@RequestParam("id") int id,
            @RequestParam(name = "filtro", defaultValue = "todas") String filtro) {
        gestor.cambiarEstado(id);
        return "redirect:/?filtro=" + filtro;
    }

    @PostMapping("/eliminar")
    public String eliminarTarea(@RequestParam("id") int id,
            @RequestParam(name = "filtro", defaultValue = "todas") String filtro,
            RedirectAttributes mensajes) {
        if (gestor.eliminarTarea(id)) {
            mensajes.addFlashAttribute("mensaje", "Tarea eliminada.");
        }
        return "redirect:/?filtro=" + filtro;
    }

    // Polimorfismo: la variable que se devuelve es de tipo Tarea,
    // pero el objeto real es de una de las clases hijas
    private Tarea crearTarea(String tipo, String titulo, String descripcion, String extra,
            LocalDate fecha, Prioridad prioridad) {
        if (extra.isEmpty()) {
            extra = "General";
        }
        switch (tipo) {
            case "Academica":
                return new TareaAcademica(titulo, descripcion, extra, fecha, prioridad);
            case "Laboral":
                return new TareaLaboral(titulo, descripcion, extra, fecha, prioridad);
            default:
                return new TareaPersonal(titulo, descripcion, extra, fecha, prioridad);
        }
    }

    // Convierte el texto del formulario (2026-11-12) en LocalDate; vacío o inválido = sin fecha
    private LocalDate convertirFecha(String texto) {
        try {
            return texto.isBlank() ? null : LocalDate.parse(texto);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
