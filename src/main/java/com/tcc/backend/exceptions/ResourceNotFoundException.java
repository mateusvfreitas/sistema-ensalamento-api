package com.tcc.backend.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String classe, Long id) {
        
        super("Objeto " + classe + " com id = " + id.toString() + " nao encontrado");
    }
}
