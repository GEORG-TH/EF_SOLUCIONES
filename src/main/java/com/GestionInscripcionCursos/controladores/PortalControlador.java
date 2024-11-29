package com.GestionInscripcionCursos.controladores;

import com.GestionInscripcionCursos.entidades.Usuario;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo) {

        try {
            usuarioServicio.registrar(nombre, email, password, password2);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Email o Contraseña invalidos!");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ALUMNO', 'ROLE_PROFESOR', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session,  RedirectAttributes redirectAttributes, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        redirectAttributes.addFlashAttribute("exito", "Logueo Exitoso!");
        
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        
        if (logueado.getRol().toString().equals("PROFESOR")) {
            return "redirect:/profesor/dashboard";
        }

        modelo.put("exito", "Logueo Exitoso!");
        return "inicio.html";
    }

    
    
    
    
}
