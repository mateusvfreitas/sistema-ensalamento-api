package com.tcc.backend.usuario;

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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<UsuarioDto> listarTodos() {
        List<Usuario> usuariosEntidade = usuarioService.listarTodos();

        return usuariosEntidade.stream().map(usuario -> modelMapper.map(usuario, UsuarioDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody UsuarioDto usuarioDto) {
        try {
            Usuario usuarioRequest = modelMapper.map(usuarioDto, Usuario.class);
            Usuario usuario = usuarioService.criarUsuario(usuarioRequest);
            UsuarioDto usuarioResponse = modelMapper.map(usuario, UsuarioDto.class);

            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarPorId(@PathVariable("id") Long id) {
        try {
            Usuario usuario = usuarioService.consultarUsuarioPorId(id);
            UsuarioDto usuarioResponse = modelMapper.map(usuario, UsuarioDto.class);
            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            Usuario usuarioRequest = modelMapper.map(usuarioDto, Usuario.class);
            Usuario usuario = usuarioService.atualizarUsuario(id, usuarioRequest);
            UsuarioDto usuarioResponse = modelMapper.map(usuario, UsuarioDto.class);
            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") Long id) {
        try {
            usuarioService.deletarUsuario(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }
}