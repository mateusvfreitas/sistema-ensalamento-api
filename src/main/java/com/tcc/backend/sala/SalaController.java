package com.tcc.backend.sala;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SalaController {
    
    @GetMapping(value="/sala")
    public String getSalas() {
        return "Endpoint OK";
    }
    
}
