package com.tcc.backend.sala.gruposala;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoSalaRepository extends JpaRepository<GrupoSala, Long> {

    List<GrupoSala> findByNomeIgnoreCase(String nome);

}
