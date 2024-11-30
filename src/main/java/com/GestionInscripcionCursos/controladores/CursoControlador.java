package com.GestionInscripcionCursos.controladores;

import com.GestionInscripcionCursos.entidades.Curso;
import com.GestionInscripcionCursos.entidades.Usuario;
import com.GestionInscripcionCursos.enumeraciones.TipoCurso;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.repositorios.UsuarioRepositorio;
import com.GestionInscripcionCursos.servicios.CursoServicio;
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
@RequestMapping("/curso")
public class CursoControlador {

    @Autowired
    private CursoServicio cursoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar() {
        return "VistaRegistrarCurso.html";
    }

    @PostMapping("/registro")
    public String registro(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            ModelMap modelo) {

        try {

            cursoServicio.crearCurso(nombre, descripcion);
            modelo.put("exito", "Curso Registrado Correctamente!");

        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());

            return "VistaRegistrarCurso.html";
        }
        return "panelAdmin.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Curso> cursos = cursoServicio.listarCursos();
        modelo.addAttribute("cursos", cursos);

        return "VistaListarCursos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(
            @PathVariable String id,
            ModelMap modelo) {

        modelo.put("curso", cursoServicio.buscarPorId(id));

        return "VistaModificarCurso.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(
            @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            RedirectAttributes redirectAttributes) {

        try {

            cursoServicio.modificarCurso(id, nombre, descripcion);

            redirectAttributes.addFlashAttribute("exito", "Curso Modificado Correctamente!");

            return "redirect:../lista";

        } catch (MyException ex) {

            redirectAttributes.addFlashAttribute("error", ex.getMessage());

            return "redirect:../modificar/" + id;
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            cursoServicio.eliminarCurso(id);

            redirectAttributes.addFlashAttribute("exito", "Curso Eliminado Correctamente!");

        } catch (MyException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:../lista";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/listaDisponiblesProfesor")
    public String listarCursosDisponiblesProfesor(ModelMap modelo) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        List<Curso> cursos = cursoServicio.listarCursosDisponiblesProfesor(usuario.getId());

        modelo.addAttribute("cursos", cursos);

        return "VistaListarCursosDisponiblesProfesor.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/listaInscritosProfesor")
    public String listarCursosInscritosProfesor(ModelMap modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        List<Curso> cursos = cursoServicio.listarCursosInscritosProfesor(usuario.getId());

        modelo.addAttribute("cursos", cursos);

        return "VistaListarCursosInscritosProfesor.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ALUMNO')")
    @GetMapping("/listaDisponiblesAlumno")
    public String listarCursosDisponiblesAlumno(ModelMap modelo) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        List<Curso> cursos = cursoServicio.listarCursosDisponiblesAlumno(usuario.getId());

        modelo.addAttribute("cursos", cursos);

        return "VistaListarCursosDisponiblesAlumno.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ALUMNO')")
    @GetMapping("/listaInscritosAlumno")
    public String listarCursosInscritosAlumno(ModelMap modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        List<Curso> cursos = cursoServicio.listarCursosInscritosAlumno(usuario.getId());

        modelo.addAttribute("cursos", cursos);

        return "VistaListarCursosInscritosAlumno.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESOR', 'ROLE_ALUMNO')")
    @GetMapping("/inscribir/{id}")
    public String inscribirCurso(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailUser = authentication.getName();

        Usuario usuario = usuarioServicio.buscarEmail(emailUser);

        try {

            cursoServicio.inscribirCurso(usuario.getId(), id);

            redirectAttributes.addFlashAttribute("exito", "Curso Inscrito Correctamente!");

        } catch (MyException ex) {

            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        if (usuario.getRol().name().equals("PROFESOR")) {
            return "redirect:../listaDisponiblesProfesor";

        } else {
            return "redirect:../listaDisponiblesAlumno";
        }

    }

}
