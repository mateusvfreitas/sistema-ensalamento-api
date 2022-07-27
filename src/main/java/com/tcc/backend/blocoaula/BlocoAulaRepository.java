package com.tcc.backend.blocoaula;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlocoAulaRepository extends JpaRepository<BlocoAula, Long>{
    
}
