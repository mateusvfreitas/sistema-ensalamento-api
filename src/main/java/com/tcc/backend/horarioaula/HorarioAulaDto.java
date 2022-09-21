package com.tcc.backend.horarioaula;

import java.time.LocalTime;

import lombok.Data;

@Data
public class HorarioAulaDto {
    private Long id;

    private String nome;

    private LocalTime horarioInicio;

    private LocalTime horarioFim;
}
