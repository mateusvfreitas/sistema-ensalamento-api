package com.tcc.backend.sala.atributosala;

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
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/atributos")
public class AtributoSalaController {

    @Autowired
    private AtributoSalaService atributoSalaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<AtributoSalaDto> listarTodos() {
        List<AtributoSala> atributoSalasEntidade = atributoSalaService.listarAtributos();

        return atributoSalasEntidade.stream().map(atributoSala -> modelMapper.map(atributoSala, AtributoSalaDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody AtributoSalaDto atributoSalaDto) {
        try {
            AtributoSala atributoSalaRequest = modelMapper.map(atributoSalaDto, AtributoSala.class);
            AtributoSala atributoSala = atributoSalaService.criarAtributoSala(atributoSalaRequest);
            AtributoSalaDto atributoSalaResponse = modelMapper.map(atributoSala, AtributoSalaDto.class);

            return new ResponseEntity<>(atributoSalaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/lista")
    public ResponseEntity<Object> criarLista(@RequestBody List<AtributoSalaDto> listaAtributoSalaDto) {
        try {
            List<AtributoSala> listaAtributoSalaRequest = listaAtributoSalaDto.stream()
                    .map(atributoSala -> modelMapper.map(atributoSala, AtributoSala.class))
                    .collect(Collectors.toList());
            List<AtributoSala> atributosCriados = atributoSalaService.criarListaAtributoSala(listaAtributoSalaRequest);
            List<AtributoSalaDto> listaAtributoSalaResponse = atributosCriados.stream()
                    .map(atributoNovo -> modelMapper.map(atributoNovo, AtributoSalaDto.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(listaAtributoSalaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            AtributoSala atributoSala = atributoSalaService.consultarAtributoSalaPorId(id);
            AtributoSalaDto atributoSalaResponse = modelMapper.map(atributoSala, AtributoSalaDto.class);
            return new ResponseEntity<>(atributoSalaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id,
            @RequestBody AtributoSalaDto atributoSalaDto) {
        try {
            AtributoSala atributoSalaRequest = modelMapper.map(atributoSalaDto, AtributoSala.class);
            AtributoSala atributoSala = atributoSalaService.atualizarAtributoSala(id, atributoSalaRequest);
            AtributoSalaDto atributoSalaResponse = modelMapper.map(atributoSala, AtributoSalaDto.class);
            return new ResponseEntity<>(atributoSalaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            atributoSalaService.deletarAtributoSala(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
