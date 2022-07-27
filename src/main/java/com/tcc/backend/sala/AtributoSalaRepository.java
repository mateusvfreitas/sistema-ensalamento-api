package com.tcc.backend.sala;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtributoSalaRepository extends JpaRepository<AtributoSala, Long> {
    
}
