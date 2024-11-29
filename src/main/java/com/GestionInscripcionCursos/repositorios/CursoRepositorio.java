package com.GestionInscripcionCursos.repositorios;

import com.GestionInscripcionCursos.entidades.Curso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepositorio extends JpaRepository<Curso, String>{
    @Query("SELECT c FROM Curso c")
    public List<Curso> buscarCursos();
    
    @Query("SELECT c FROM Curso c WHERE c.id = :id")
    public Curso buscarPorId(@Param("id") String id);
}
