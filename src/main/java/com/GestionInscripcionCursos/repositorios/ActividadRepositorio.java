package com.GestionInscripcionCursos.repositorios;

import com.GestionInscripcionCursos.entidades.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepositorio extends JpaRepository<Actividad,String>{
    
}
