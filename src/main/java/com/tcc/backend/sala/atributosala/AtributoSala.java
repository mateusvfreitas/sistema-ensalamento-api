package com.tcc.backend.sala.atributosala;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcc.backend.blocoaula.BlocoAula;
import com.tcc.backend.sala.Sala;

import lombok.Data;

@Data
@Entity
@Table
public class AtributoSala {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    @JsonIgnore
    @ManyToMany(mappedBy = "atributos")
    private List<Sala> salas;

    @JsonIgnore
    @ManyToMany(mappedBy = "atributosSala")
    private List<BlocoAula> blocosAula;
}
