package com.tcc.backend.usuario;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcc.backend.curso.Curso;

import lombok.Data;

@Data
@Entity
@Table
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private Boolean isAdmin;

    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "usuarios")
    private List<Curso> cursos;
}
