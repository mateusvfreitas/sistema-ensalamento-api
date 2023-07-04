package com.tcc.backend.ensalamento;

import java.util.List;

import lombok.Data;

@Data
public class EnsalamentoDto {

    private String nomeHorario;

    private List<EnsalamentoDetalheDto> listaDetalhes;
}
