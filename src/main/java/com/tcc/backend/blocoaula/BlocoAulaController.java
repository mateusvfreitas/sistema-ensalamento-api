package com.tcc.backend.blocoaula;

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
@RequestMapping("/blocos-aula")
public class BlocoAulaController {
    
    @Autowired
    private BlocoAulaService blocoAulaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<BlocoAulaDto> listarTodos() {
        List<BlocoAula> blocoAulaEntidade = blocoAulaService.listarTodos();

        return blocoAulaEntidade.stream().map(blocoAula -> modelMapper.map(blocoAula, BlocoAulaDto.class)).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody BlocoAulaDto blocoAulaDto) {
        try {
            BlocoAula blocoAulaRequest = modelMapper.map(blocoAulaDto, BlocoAula.class);
            BlocoAula blocoAula = blocoAulaService.criarBlocoAula(blocoAulaRequest);
            BlocoAulaDto blocoAulaResponse = modelMapper.map(blocoAula, BlocoAulaDto.class);
        
            return new ResponseEntity<>(blocoAulaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            BlocoAula blocoAula = blocoAulaService.consultarBlocoAulaPorId(id);
            BlocoAulaDto blocoAulaResponse = modelMapper.map(blocoAula, BlocoAulaDto.class);
            return new ResponseEntity<>(blocoAulaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id,
            @RequestBody BlocoAulaDto blocoAulaDto) {
        try {
            BlocoAula blocoAulaRequest = modelMapper.map(blocoAulaDto, BlocoAula.class);
            BlocoAula blocoAula = blocoAulaService.atualizarBlocoAula(id, blocoAulaRequest);
            BlocoAulaDto blocoAulaResponse = modelMapper.map(blocoAula, BlocoAulaDto.class);
            return new ResponseEntity<>(blocoAulaResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            blocoAulaService.deletarBlocoAula(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
