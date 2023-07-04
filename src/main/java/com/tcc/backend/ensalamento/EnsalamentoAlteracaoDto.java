package com.tcc.backend.ensalamento;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EnsalamentoAlteracaoDto {

    @JsonProperty("codigo")
    String codigo;

    @JsonProperty("horario")
    String horario;

    @JsonProperty("salaOriginal")
    String salaOriginal;

    @JsonProperty("salaNova")
    String salaNova;
}
