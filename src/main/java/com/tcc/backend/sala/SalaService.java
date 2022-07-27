package com.tcc.backend.sala;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SalaService {
    
    private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<Sala> getSalas(){
        return salaRepository.findAll();
    }
}
