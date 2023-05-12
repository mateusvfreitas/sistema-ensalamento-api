package com.tcc.backend.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String classe) {

        super(classe + " não encontrado");
    }
}
