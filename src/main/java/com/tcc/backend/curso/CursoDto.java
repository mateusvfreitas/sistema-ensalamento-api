package com.tcc.backend.curso;

import java.util.List;

import com.tcc.backend.usuario.UsuarioDto;

import lombok.Data;

@Data
public class CursoDto {
    private Long id;

    private String nome;

    private List<UsuarioDto> usuarios;
}
