package com.GestionInscripcionCursos.repositorios;

import com.GestionInscripcionCursos.entidades.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepositorio extends JpaRepository<Reporte,String>{
    
}