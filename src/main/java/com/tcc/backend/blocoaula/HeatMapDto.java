package com.tcc.backend.blocoaula;

import java.util.List;

import lombok.Data;

@Data
public class HeatMapDto {

    private String nomeHorario;

    private List<HeatMapDetalhe> listaDetalhes;

    private float media;
}
