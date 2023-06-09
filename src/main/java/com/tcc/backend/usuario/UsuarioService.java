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
        List<Usuario> listaUsuariosNome = usuarioRepository.findByNomeIgnoreCase(usuario.getNome());
        List<Usuario> listaUsuariosEmail = usuarioRepository.findByEmailIgnoreCase(usuario.getNome());
        List<Usuario> listaUsuariosUsername = usuarioRepository.findByUsernameIgnoreCase(usuario.getNome());

        if (!listaUsuariosNome.isEmpty()) {
            throw new ResourceAlreadyExists("Usuario", "nome", usuario.getNome());
        } else if (!listaUsuariosEmail.isEmpty()) {
            throw new ResourceAlreadyExists("Usuario", "email", usuario.getEmail());
        } else if (!listaUsuariosUsername.isEmpty()) {
            throw new ResourceAlreadyExists("Usuario", "username", usuario.getUsername());
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario consultarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario"));
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioReq) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario"));

        if ((!usuario.getNome().equals(usuarioReq.getNome()))
                && (!usuarioRepository.findByNomeIgnoreCase(usuarioReq.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("Usuario", "nome", usuarioReq.getNome());
        }
        usuario.setIsAdmin(usuarioReq.getIsAdmin());
        usuario.setNome(usuarioReq.getNome());
        usuario.setEmail(usuarioReq.getEmail());
        usuario.setUsername(usuarioReq.getUsername());
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        if (usuarioRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Usuario");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario consultarPorUsername(String username) {
        List<Usuario> listaUsuariosUsername = usuarioRepository.findByUsernameIgnoreCase(username);

        if (listaUsuariosUsername.isEmpty()) {
            throw new ResourceNotFoundException("Usuario");
        }

        return listaUsuariosUsername.get(0);
    }

}
