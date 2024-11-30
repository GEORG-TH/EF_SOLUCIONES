package com.GestionInscripcionCursos.controladores;

import com.GestionInscripcionCursos.entidades.Actividad;
import com.GestionInscripcionCursos.entidades.Reporte;
import com.GestionInscripcionCursos.entidades.Usuario;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.servicios.ActividadServicio;
import com.GestionInscripcionCursos.servicios.ReporteServicio;
import com.GestionInscripcionCursos.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reporte")
public class ReporteControlador {

    @Autowired
    private ActividadServicio actividadServicio;

    @Autowired
    private ReporteServicio reporteServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_ALUMNO')")
    @GetMapping("/registrar/{id}")
    public String registrar(
            @PathVariable String id,
            ModelMap modelo,
            RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        Actividad actividad = actividadServicio.buscarPorId(id);
        try {
            reporteServicio.validarDobleReporte(usuario.getId(), actividad.getId());

            modelo.put("actividad", actividad);

            return "VistaRegistrarReporte.html";

        } catch (MyException ex) {

            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/actividad/listar/" + actividad.getCurso().getId();
        }

    }

    @PostMapping("/registro/{id}")
    public String registro(
            @PathVariable String id,
            @RequestParam String respuesta,
            RedirectAttributes redirectAttributes) {

        Actividad actividad = actividadServicio.buscarPorId(id);

        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String emailUser = authentication.getName();

            Usuario usuario = usuarioServicio.buscarEmail(emailUser);

            reporteServicio.crearReporte(respuesta, id, usuario.getId());
            redirectAttributes.addFlashAttribute("exito", "Reporte Registrado Correctamente!");
            return "redirect:/actividad/listar/" + actividad.getCurso().getId();

        } catch (MyException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/reporte/registrar/" + actividad.getId();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/listar/{id}")
    public String listar(
            @PathVariable String id,
            ModelMap modelo) {

        List<Reporte> reportes = reporteServicio.listarReportesPorIdActividad(id);
        modelo.addAttribute("reportes", reportes);

        return "VistaListarReportes.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/calificar/{id}")
    public String calificar(
            @PathVariable String id,
            ModelMap modelo) {

        modelo.put("reporte", reporteServicio.buscarPorId(id));

        return "VistaCalificarReporte.html";
    }

    @PostMapping("/calificar/{id}")
    public String calificar(
            @PathVariable String id,
            @RequestParam String nota,
            @RequestParam String comentario,
            RedirectAttributes redirectAttributes) {

        Reporte reporte = reporteServicio.buscarPorId(id);

        try {

            reporteServicio.calificarReporte(id, nota, comentario);

            redirectAttributes.addFlashAttribute("exito", "Reporte Calificado Correctamente!");

            return "redirect:/reporte/listar/" + reporte.getActividad().getId();

        } catch (MyException ex) {

            redirectAttributes.addFlashAttribute("error", ex.getMessage());

            return "redirect:/reporte/calificar/" + reporte.getId();
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ALUMNO')")
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable String id, ModelMap modelo) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        Actividad actividad = actividadServicio.buscarPorId(id);

        Reporte reporte = reporteServicio.buscarPorIdCategoriaIdUsuario(usuario.getId(), actividad.getId());

        modelo.put("reporte", reporte);

        return "reporteDetalle.html";

    }

}
