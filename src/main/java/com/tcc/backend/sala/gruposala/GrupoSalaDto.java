package com.tcc.backend.sala.gruposala;

import com.tcc.backend.usuario.UsuarioDto;

import lombok.Data;

@Data
public class GrupoSalaDto {

    private Long id;

    private String nome;

    // private List<SalaDto> salas;
    // "salas": [
    // {
    // "id": 4444
    // }
    // ],

    private UsuarioDto usuarioResponsavel;
}
