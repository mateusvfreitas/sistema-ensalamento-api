package com.tcc.backend.sala;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.backend.sala.atributosala.AtributoSala;
import com.tcc.backend.sala.atributosala.AtributoSalaService;
import com.tcc.backend.sala.gruposala.GrupoSala;
import com.tcc.backend.sala.gruposala.GrupoSalaService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @Autowired
    private AtributoSalaService atributoSalaService;

    @Autowired
    private GrupoSalaService grupoSalaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<SalaDto> listarTodos(@RequestParam(value = "atributo", required = false) List<Long> idsAtributos,
            @RequestParam(value = "capacidadeMinima", required = false) Integer capacidade,
            @RequestParam(value = "agrupamento", required = false) List<Long> idsGrupos,
            @RequestParam(value = "isMatchAll", required = false) Boolean isMatchAll) {
        List<Sala> salaEntidade;
        List<AtributoSala> listaAtributos = null;
        List<GrupoSala> listaGrupos = null;

        if (idsAtributos == null && capacidade == null && idsGrupos == null) {
            salaEntidade = salaService.listarSalas();
        }
        if (idsAtributos != null) {
            listaAtributos = atributoSalaService.buscarAtributosPorId(idsAtributos);
        }
        if (idsGrupos != null) {
            listaGrupos = grupoSalaService.buscarGruposPorId(idsGrupos);
        }

        salaEntidade = salaService.listarSalasFiltros(listaAtributos, capacidade, listaGrupos, isMatchAll);

        return salaEntidade.stream().map(sala -> modelMapper.map(sala, SalaDto.class)).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody SalaDto salaDto) {
        try {
            Sala salaRequest = modelMapper.map(salaDto, Sala.class);
            Sala sala = salaService.criarSala(salaRequest);
            SalaDto salaResponse = modelMapper.map(sala, SalaDto.class);

            return new ResponseEntity<>(salaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            Sala sala = salaService.consultarSalaPorId(id);
            SalaDto salaResponse = modelMapper.map(sala, SalaDto.class);
            return new ResponseEntity<>(salaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id, @RequestBody SalaDto salaDto) {
        try {
            Sala salaRequest = modelMapper.map(salaDto, Sala.class);
            Sala sala = salaService.atualizarSala(id, salaRequest);
            SalaDto salaResponse = modelMapper.map(sala, SalaDto.class);
            return new ResponseEntity<>(salaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            salaService.deletarSala(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
