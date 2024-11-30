/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.GestionInscripcionCursos.controladores;

import com.GestionInscripcionCursos.entidades.Inscripcion;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.servicios.InscripcionServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inscripcion")
public class InscripcionControlador {
    
    @Autowired
    private InscripcionServicio inscripcionServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listaPendientesProfesor")
    public String listaPendientesProfesor(ModelMap modelo) {
        List<Inscripcion> inscripciones = inscripcionServicio.listaPendientesProfesor();
        modelo.addAttribute("inscripciones", inscripciones);
        return "VistaListarInscripcionesPendientesProfesor.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listaRealizadasProfesor")
    public String listaRealizadasProfesor(ModelMap modelo) {
        List<Inscripcion> inscripciones = inscripcionServicio.listaRealizadasProfesor();
        modelo.addAttribute("inscripciones", inscripciones);

        return "VistaListarInscripcionesRealizadasProfesor.html";
    }
    
    
    
    
    @PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/listaPendientesAlumno")
    public String listaPendientesAlumno(ModelMap modelo) {
        List<Inscripcion> inscripciones = inscripcionServicio.listaPendientesAlumno();
        modelo.addAttribute("inscripciones", inscripciones);
        return "VistaListarInscripcionesPendientesProfesor.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PROFESOR')")
    @GetMapping("/listaRealizadasAlumno")
    public String listaRealizadasAlumno(ModelMap modelo) {
        List<Inscripcion> inscripciones = inscripcionServicio.listaRealizadasAlumno();
        modelo.addAttribute("inscripciones", inscripciones);

        return "VistaListarInscripcionesRealizadasProfesor.html";
    }
    
    
    
    
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROFESOR')")
    @GetMapping("/aprobar/{id}")
    public String aprobar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            inscripcionServicio.aprobar(id);

            redirectAttributes.addFlashAttribute("exito", "Inscripcion Aprobada correctamente!");

        } catch (MyException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:../listaPendientesProfesor";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROFESOR')")
    @GetMapping("/rechazar/{id}")
    public String rechazar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            inscripcionServicio.rechazar(id);

            redirectAttributes.addFlashAttribute("exito", "Inscripcion Rechazada correctamente!");

        } catch (MyException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:../listaPendientesProfesor";
    }
    
    
}
