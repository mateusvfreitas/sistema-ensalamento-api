package com.tcc.backend.usuario;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
 
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UsuarioDto> listarTodos() {
        List<Usuario> usuariosEntidade = usuarioRepository.findAll();
        Type listType = new TypeToken<List<UsuarioDto>>(){}.getType();

        List<UsuarioDto> usuariosDto = modelMapper.map(usuariosEntidade, listType);
        return usuariosDto;
    }

    
}
