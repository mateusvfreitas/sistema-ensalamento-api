package com.tcc.backend.sala;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.tcc.backend.sala.atributosala.AtributoSala;

import lombok.Data;

@Data
@Entity
@Table
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private Integer capacidade;
    
    @ManyToMany
    @JoinTable(
        name = "sala_atributo_sala", 
        joinColumns = @JoinColumn(name = "id_sala"), 
        inverseJoinColumns = @JoinColumn(name = "id_atributo_sala"))
    private List<AtributoSala> atributos;

    private Boolean isExclusiva;
    
    private Boolean isLiberar;
}
