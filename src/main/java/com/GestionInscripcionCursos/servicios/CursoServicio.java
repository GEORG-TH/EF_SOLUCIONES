package com.GestionInscripcionCursos.servicios;

import com.GestionInscripcionCursos.entidades.Actividad;
import com.GestionInscripcionCursos.entidades.Curso;
import com.GestionInscripcionCursos.entidades.Inscripcion;
import com.GestionInscripcionCursos.enumeraciones.TipoCurso;
import com.GestionInscripcionCursos.excepciones.MyException;
import com.GestionInscripcionCursos.repositorios.CursoRepositorio;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoServicio {

    @Autowired
    private CursoRepositorio cursoRepositorio;

    @Transactional
    public void crearCurso(String nombre, String descripcion) throws MyException {

        validarCurso(nombre, descripcion);
        
        Curso curso = new Curso(nombre, descripcion);
        
        cursoRepositorio.save(curso);
    }
    
    public List<Curso> listarCursos() {
        return cursoRepositorio.buscarCursos();
    }
    
    
    @Transactional
    public void modificarCurso(String id,String nombre, String descripcion) throws MyException {

        validarCurso(nombre, descripcion);

        Optional<Curso> respuesta = cursoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Curso curso = respuesta.get();
            
            curso.setNombre(nombre);
            
            curso.setDescripcion(descripcion);         
            
            cursoRepositorio.save(curso);

        }
    }
    
    @Transactional
    public void eliminarCurso(String id) throws MyException {

        Curso curso = cursoRepositorio.getById(id);

        cursoRepositorio.delete(curso);

    }
    
    public Curso buscarPorId(String id){
        return cursoRepositorio.buscarPorId(id);
    }
    

    private void validarCurso(String nombre, String descripcion) throws MyException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("El nombre no puede ser nulo o estar vacio");
        }

        if (descripcion.isEmpty() || descripcion == null) {
            throw new MyException("La descripcion no puede ser nulo o estar vacio");
        }
    }
}
