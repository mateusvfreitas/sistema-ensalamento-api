package com.tcc.backend.sala;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcc.backend.sala.atributosala.AtributoSala;
import com.tcc.backend.sala.gruposala.GrupoSala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByNomeIgnoreCase(String nome);

    @Query("SELECT distinct s FROM Sala s "
            + "JOIN s.atributos atr "
            + "JOIN s.grupoSala gs "
            + "WHERE ( (:filtroAtributos) is null or atr in (:filtroAtributos) ) "
            + "AND ( :filtroCapacidade is null or s.capacidade >= :filtroCapacidade ) "
            + "AND ( (:filtroGrupos) is null or gs in (:filtroGrupos) )")
    List<Sala> findByFiltrosGeneric(@Param("filtroAtributos") List<AtributoSala> filtroAtributos,
            @Param("filtroCapacidade") Integer filtroCapacidade, @Param("filtroGrupos") List<GrupoSala> filtroGrupos);

    @Query("SELECT distinct s FROM Sala s "
            + "JOIN s.atributos atr "
            + "JOIN s.grupoSala gs "
            + "WHERE ( (:filtroAtributos) is null or atr in (:filtroAtributos) ) "
            + "AND ( :filtroCapacidade is null or s.capacidade >= :filtroCapacidade ) "
            + "AND ( (:filtroGrupos) is null or gs in (:filtroGrupos) ) "
            + "GROUP BY s HAVING COUNT(s) = :qtdeAtributos ")
    List<Sala> findByFiltrosMatchAll(@Param("filtroAtributos") List<AtributoSala> filtroAtributos,
            @Param("filtroCapacidade") Integer filtroCapacidade, @Param("filtroGrupos") List<GrupoSala> filtroGrupos,
            @Param("qtdeAtributos") Long qtdeAtributos);

}
