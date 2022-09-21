package com.tcc.backend.horarioaula;

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
@RequestMapping("/horarios")
public class HorarioAulaController {

    @Autowired
    private HorarioAulaService horarioAulaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<HorarioAulaDto> listarTodos() {
        List<HorarioAula> horarioEntidade = horarioAulaService.listarTodos();

        return horarioEntidade.stream().map(horarioAula -> modelMapper.map(horarioAula, HorarioAulaDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody HorarioAulaDto horarioAulaDto) {
        try {
            HorarioAula horarioRequest = modelMapper.map(horarioAulaDto, HorarioAula.class);
            HorarioAula horario = horarioAulaService.criarHorarioAula(horarioRequest);
            HorarioAulaDto horarioResponse = modelMapper.map(horario, HorarioAulaDto.class);

            return new ResponseEntity<>(horarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            HorarioAula horario = horarioAulaService.consultarHorarioPorId(id);
            HorarioAulaDto horarioResponse = modelMapper.map(horario, HorarioAulaDto.class);
            return new ResponseEntity<>(horarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id, @RequestBody HorarioAulaDto horarioAulaDto) {
        try {
            HorarioAula horarioRequest = modelMapper.map(horarioAulaDto, HorarioAula.class);
            HorarioAula horario = horarioAulaService.atualizarHorarioAula(id, horarioRequest);
            HorarioAulaDto horarioResponse = modelMapper.map(horario, HorarioAulaDto.class);
            return new ResponseEntity<>(horarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            horarioAulaService.deletarHorarioAula(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
