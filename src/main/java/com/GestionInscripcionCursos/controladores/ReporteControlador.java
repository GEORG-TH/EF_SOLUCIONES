package com.GestionInscripcionCursos.controladores;

import com.GestionInscripcionCursos.entidades.Usuario;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.servicios.ActividadServicio;
import com.GestionInscripcionCursos.servicios.ReporteServicio;
import com.GestionInscripcionCursos.servicios.UsuarioServicio;
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
            ModelMap modelo) {

        modelo.put("actividad", actividadServicio.buscarPorId(id));

        return "VistaRegistrarReporte.html";
    }

    @PostMapping("/registro/{id}")
    public String registro(
            @PathVariable String id,
            @RequestParam String respuesta,
            RedirectAttributes redirectAttributes) {

        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String emailUser = authentication.getName();

            Usuario usuario = usuarioServicio.buscarEmail(emailUser);

            reporteServicio.crearReporte(respuesta, id, usuario.getId());
            redirectAttributes.addFlashAttribute("exito", "Reporte Registrado Correctamente!");
            return "redirect:/actividad/listaInscritosAlumno";
        } catch (MyException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/reporte/registrar/" + id;
        }
    }

    /*@PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/listar/{id}")
    public String listar(
            @PathVariable String id,
            ModelMap modelo) {

        List<Actividad> actividades = actividadServicio.listarActividadesPorIdCurso(id);
        modelo.addAttribute("actividades", actividades);

        return "VistaListarActividades.html";
    }*/
}
