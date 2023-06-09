package com.tcc.backend.blocoaula;

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

import com.tcc.backend.enums.EnDiaSemana;
import com.tcc.backend.sala.atributosala.AtributoSala;
import com.tcc.backend.sala.atributosala.AtributoSalaService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/blocos-aula")
public class BlocoAulaController {

    @Autowired
    private BlocoAulaService blocoAulaService;

    @Autowired
    private AtributoSalaService atributoSalaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<BlocoAulaDto> listarTodos(@RequestParam(value = "responsavel", required = false) String responsavel,
            @RequestParam(value = "atributo", required = false) List<Long> idsAtributos,
            @RequestParam(value = "diaSemana", required = false) EnDiaSemana diaSemana,
            @RequestParam(value = "isMatchAll", required = false) Boolean isMatchAll) {
        List<BlocoAula> blocoAulaEntidade;
        List<AtributoSala> listaAtributos = null;
        EnDiaSemana dia = null;
        if (idsAtributos != null) {
            listaAtributos = atributoSalaService.buscarAtributosPorId(idsAtributos);
        }
        if (diaSemana != null) {
            dia = diaSemana;
        }
        // if (responsavel != null) {
        // blocoAulaEntidade = blocoAulaService.findByUsuarioResponsavel(responsavel);
        // } else {
        // blocoAulaEntidade = blocoAulaService.listarTodos();
        // }
        blocoAulaEntidade = blocoAulaService.listarAulasFiltros(responsavel, listaAtributos, dia, isMatchAll);

        return blocoAulaEntidade.stream().map(blocoAula -> modelMapper.map(blocoAula, BlocoAulaDto.class))
                .collect(Collectors.toList());
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

    @GetMapping("/heatmap")
    public List<HeatMapDto> gerarHeatMap(@RequestParam(value = "atributo", required = false) List<Long> idsAtributos) {
        List<AtributoSala> listaAtributos = null;

        if (idsAtributos != null) {
            listaAtributos = atributoSalaService.buscarAtributosPorId(idsAtributos);
        }
        return blocoAulaService.gerarHeatMap(listaAtributos);
    }
}
