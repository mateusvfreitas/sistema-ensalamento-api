package com.tcc.backend.sala.atributosala;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtributoSalaRepository extends JpaRepository<AtributoSala, Long> {
    
    List<AtributoSala> findByNomeIgnoreCase(String nome);
}
