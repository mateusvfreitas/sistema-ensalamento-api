package com.tcc.backend.sala.gruposala;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grupo-salas")
public class GrupoSalaController {

    @Autowired
    private GrupoSalaService grupoSalaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<GrupoSalaDto> listarTodos() {
        List<GrupoSala> gruposEntidade = grupoSalaService.listarGrupos();

        return gruposEntidade.stream().map(grupo -> modelMapper.map(grupo,
                GrupoSalaDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody GrupoSalaDto grupoDto) {
        try {
            GrupoSala grupoRequest = modelMapper.map(grupoDto, GrupoSala.class);
            GrupoSala grupo = grupoSalaService.criarGrupo(grupoRequest);
            GrupoSalaDto grupoResponse = modelMapper.map(grupo, GrupoSalaDto.class);

            return new ResponseEntity<>(grupoResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            GrupoSala grupo = grupoSalaService.consultarGrupoPorId(id);
            GrupoSalaDto grupoResponse = modelMapper.map(grupo, GrupoSalaDto.class);
            return new ResponseEntity<>(grupoResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id, @RequestBody GrupoSalaDto grupoDto) {
        try {
            GrupoSala grupoRequest = modelMapper.map(grupoDto, GrupoSala.class);
            GrupoSala grupo = grupoSalaService.atualizarGrupo(id, grupoRequest);
            GrupoSalaDto grupoResponse = modelMapper.map(grupo, GrupoSalaDto.class);
            return new ResponseEntity<>(grupoResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            grupoSalaService.deletarGrupo(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
