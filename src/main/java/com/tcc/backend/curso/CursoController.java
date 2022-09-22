package com.tcc.backend.curso;

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
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<CursoDto> listarTodos() {
        List<Curso> cursosEntidade = cursoService.listarTodos();

        return cursosEntidade.stream().map(curso -> modelMapper.map(curso, CursoDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody CursoDto cursoDto) {
        try {
            Curso cursoRequest = modelMapper.map(cursoDto, Curso.class);
            Curso curso = cursoService.criarCurso(cursoRequest);
            CursoDto cursoResponse = modelMapper.map(curso, CursoDto.class);

            return new ResponseEntity<>(cursoResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            Curso curso = cursoService.consultarCursoPorId(id);
            CursoDto cursoResponse = modelMapper.map(curso, CursoDto.class);
            return new ResponseEntity<>(cursoResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id,
            @RequestBody CursoDto cursoDto) {
        try {
            Curso cursoRequest = modelMapper.map(cursoDto, Curso.class);
            Curso curso = cursoService.atualizarCurso(id, cursoRequest);
            CursoDto cursoResponse = modelMapper.map(curso, CursoDto.class);
            return new ResponseEntity<>(cursoResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            cursoService.deletarCurso(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
