package com.tcc.backend.blocoaula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcc.backend.enums.EnDiaSemana;
import com.tcc.backend.sala.Sala;
import com.tcc.backend.sala.atributosala.AtributoSala;

@Repository
public interface BlocoAulaRepository extends JpaRepository<BlocoAula, Long> {

    @Query("SELECT distinct ba FROM BlocoAula ba "
            + "JOIN ba.atributosSala atr "
            + "JOIN ba.curso c "
            + "JOIN c.usuarios u "
            + "WHERE ( :responsavel is null or u.username = :responsavel ) "
            + "AND ( (:filtroAtributos) is null or atr in (:filtroAtributos) ) "
            + "AND ( (:filtroDiaSemana) is null or ba.diaSemana = :filtroDiaSemana ) ")
    List<BlocoAula> findByUsuarioResponsavelGeneric(@Param("responsavel") String responsavel,
            @Param("filtroAtributos") List<AtributoSala> filtroAtributos,
            @Param("filtroDiaSemana") EnDiaSemana filtroDiaSemana);

    @Query("SELECT distinct ba FROM BlocoAula ba "
            + "JOIN ba.atributosSala atr "
            + "WHERE ( (:filtroAtributos) is null or atr in (:filtroAtributos) ) "
            + "AND ( :filtroDiaSemana is null or ba.diaSemana = :filtroDiaSemana ) "
            + "GROUP BY ba HAVING COUNT(ba) = :qtdeAtributos ")
    List<BlocoAula> findByFiltrosMatchAll(@Param("filtroAtributos") List<AtributoSala> filtroAtributos,
            @Param("filtroDiaSemana") EnDiaSemana filtroDiaSemana,
            @Param("qtdeAtributos") Long qtdeAtributos);

    @Query("SELECT distinct ba FROM BlocoAula ba "
            + "JOIN ba.atributosSala atr "
            + "JOIN ba.curso c "
            + "JOIN c.usuarios u "
            + "WHERE ( :responsavel is null or u.username = :responsavel ) "
            + "AND ( (:filtroAtributos) is null or atr in (:filtroAtributos) ) "
            + "AND ( :filtroDiaSemana is null or ba.diaSemana = :filtroDiaSemana ) "
            + "GROUP BY ba HAVING COUNT(ba) = :qtdeAtributos ")
    List<BlocoAula> findByFiltrosAndUsuarioMatchAll(@Param("responsavel") String responsavel,
            @Param("filtroAtributos") List<AtributoSala> filtroAtributos,
            @Param("filtroDiaSemana") EnDiaSemana filtroDiaSemana,
            @Param("qtdeAtributos") Long qtdeAtributos);

    List<BlocoAula> findBySalaAtual(Sala salaAtual);

}
