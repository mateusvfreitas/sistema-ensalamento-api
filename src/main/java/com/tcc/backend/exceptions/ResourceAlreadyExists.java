package com.tcc.backend.exceptions;

public class ResourceAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExists(String classe, String campo, String valor) {
        
        super("Ja existe um " + classe + " com " + campo + " = " + valor);
    }
    
}
