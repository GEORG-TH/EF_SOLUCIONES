package com.GestionInscripcionCursos.repositorios;

import com.GestionInscripcionCursos.entidades.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepositorio extends JpaRepository<Inscripcion,String>{
    
}
