package com.tcc.backend.sala;

import java.util.List;

import com.tcc.backend.sala.atributosala.AtributoSalaDto;
import com.tcc.backend.sala.gruposala.GrupoSalaDto;

import lombok.Data;

@Data
public class SalaDto {
    private Long id;

    private String nome;

    private Integer capacidade;

    private List<AtributoSalaDto> atributos;

    private Boolean isExclusiva;

    private Boolean isLiberar;

    private GrupoSalaDto grupoSala;
}
