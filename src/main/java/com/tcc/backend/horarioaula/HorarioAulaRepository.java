package com.tcc.backend.horarioaula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioAulaRepository extends JpaRepository<HorarioAula, Long>{
    
    List<HorarioAula> findByNomeIgnoreCase(String nome);
    
}