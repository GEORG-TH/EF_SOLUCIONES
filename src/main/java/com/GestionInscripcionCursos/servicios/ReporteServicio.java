package com.GestionInscripcionCursos.servicios;

import com.GestionInscripcionCursos.entidades.Actividad;
import com.GestionInscripcionCursos.entidades.Reporte;
import com.GestionInscripcionCursos.entidades.Usuario;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.repositorios.ActividadRepositorio;
import com.GestionInscripcionCursos.repositorios.ReporteRepositorio;
import com.GestionInscripcionCursos.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReporteServicio {

    @Autowired
    private ActividadRepositorio actividadRepositorio;

    @Autowired
    private ReporteRepositorio reporteRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearReporte(String respuesta, String idActividad, String idUser) throws MyException {

        validarReporte(respuesta);

        Optional<Actividad> respuesta1 = actividadRepositorio.findById(idActividad);

        Actividad actividad = respuesta1.get();
        
        Optional<Usuario> respuesta2 = usuarioRepositorio.findById(idUser);

        Usuario usuario = respuesta2.get();

        Reporte reporte = new Reporte(respuesta, "Por Calificar", "Ningun Comentario", "ENVIADO", new Date(),usuario, actividad);

        reporteRepositorio.save(reporte);
    }

    /*--------------------*/
    public List<Actividad> listarActividadesPorIdCurso(String idCurso) {
        return actividadRepositorio.buscarActividadesPorIdCurso(idCurso);
    }

    /*--------------------*/




    public Actividad buscarPorId(String id) {
        return actividadRepositorio.buscarPorId(id);
    }

    private void validarReporte(String respuesta) throws MyException {

        if (respuesta.isEmpty() || respuesta == null) {
            throw new MyException("La respuesta no puede ser nulo o estar vacio");
        }

    }
}
