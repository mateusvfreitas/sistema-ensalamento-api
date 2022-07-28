package com.tcc.backend.usuario;

import lombok.Data;

@Data
public class UsuarioDto {
    private Long id;

    private String nome;
    
    private Boolean isAdmin;
}
