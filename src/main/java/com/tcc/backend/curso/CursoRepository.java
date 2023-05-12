package com.tcc.backend.curso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByNomeIgnoreCase(String nome);

    @Query("SELECT distinct c FROM Curso c "
            + "JOIN c.usuarios u "
            + "WHERE u.username = :responsavel ")
    List<Curso> findByUsuarioResponsavel(@Param("responsavel") String responsavel);
}