package com.tcc.backend.enums;

public enum EnDiaSemana {

    SABADO("Sábado"), DOMINGO("Domingo"), SEGUNDA("Segunda"), TERCA("Terça"), QUARTA("Quarta"), QUINTA("Quinta"),
    SEXTA("Sexta");

    private String descricao;

    private EnDiaSemana(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
