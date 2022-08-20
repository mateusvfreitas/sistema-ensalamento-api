package com.tcc.backend.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario criarUsuario(Usuario usuario) {
        List<Usuario> listaUsuarios = usuarioRepository.findByNomeIgnoreCase(usuario.getNome());

        if (!listaUsuarios.isEmpty()) {
            throw new ResourceAlreadyExists("Usuario", "nome", usuario.getNome());
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario consultarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioReq) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if ((!usuario.getNome().equals(usuarioReq.getNome()))
                && (!usuarioRepository.findByNomeIgnoreCase(usuarioReq.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("Usuario", "nome", usuarioReq.getNome());
        }
        usuario.setIsAdmin(usuarioReq.getIsAdmin());
        usuario.setNome(usuarioReq.getNome());
        usuario.setEmail(usuarioReq.getEmail());
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        if (usuarioRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        usuarioRepository.deleteById(id);
    }

}
