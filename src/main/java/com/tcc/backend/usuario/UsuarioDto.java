package com.tcc.backend.usuario;

import lombok.Data;

@Data
public class UsuarioDto {
    private Long id;

    private String nome;

    private Boolean isAdmin;

    private String email;

    private String username;
}
