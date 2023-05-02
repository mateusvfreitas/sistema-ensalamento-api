package com.tcc.backend.blocoaula;

import java.util.List;

import com.tcc.backend.curso.CursoDto;
import com.tcc.backend.horarioaula.HorarioAulaDto;
import com.tcc.backend.sala.SalaDto;
import com.tcc.backend.sala.atributosala.AtributoSalaDto;

import lombok.Data;

@Data
public class BlocoAulaDto {
    private Long id;

    private CursoDto curso;

    private String disciplina;

    private String turma;

    private String diaSemana;

    private HorarioAulaDto horarioInicio;

    private HorarioAulaDto horarioFim;

    private List<AtributoSalaDto> atributosSala;

    private SalaDto salaEspecifica;

    private SalaDto SalaAtual;

    public String getDiaSemana() {
        return diaSemana;
    }

}
