package com.tcc.backend.blocoaula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcc.backend.sala.atributosala.AtributoSala;

@Repository
public interface BlocoAulaRepository extends JpaRepository<BlocoAula, Long> {

    @Query("SELECT distinct ba FROM BlocoAula ba "
            + "JOIN ba.curso c "
            + "JOIN c.usuarios u "
            + "WHERE u.username = :responsavel ")
    List<BlocoAula> findByUsuarioResponsavel(@Param("responsavel") String responsavel);

    @Query("SELECT distinct ba FROM BlocoAula ba "
            + "JOIN ba.atributosSala atr "
            + "WHERE ( (:filtroAtributos) is null or atr in (:filtroAtributos) ) "
            + "GROUP BY ba HAVING COUNT(ba) = :qtdeAtributos ")
    List<BlocoAula> findByFiltrosMatchAll(@Param("filtroAtributos") List<AtributoSala> filtroAtributos,
            @Param("qtdeAtributos") Long qtdeAtributos);

}
