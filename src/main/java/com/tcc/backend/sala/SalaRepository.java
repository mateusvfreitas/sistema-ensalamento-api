package com.tcc.backend.sala;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByNomeIgnoreCase(String nome);

}
