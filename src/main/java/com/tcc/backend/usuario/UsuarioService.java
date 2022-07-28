package com.tcc.backend.usuario;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

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

    public UsuarioDto criarUsuario(UsuarioDto usuarioDto){
        Usuario usuario = usuarioRepository.save(modelMapper.map(usuarioDto, Usuario.class));

        UsuarioDto usuarioResponse = modelMapper.map(usuario, UsuarioDto.class);

        return usuarioResponse;
    }

    public UsuarioDto consultarPorId(Long id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if(usuario.isPresent()){
            return modelMapper.map(usuario.get(), UsuarioDto.class);
        } else{
            return null;
        }
    }

    public UsuarioDto atualizarUsuario(Long id, UsuarioDto usuarioDto){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setIsAdmin(usuarioDto.getIsAdmin());
        usuario.setNome(usuarioDto.getNome());
        usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDto.class);
    }
    
    public void deletarUsuario(Long id){
        usuarioRepository.deleteById(id);
    }
}
