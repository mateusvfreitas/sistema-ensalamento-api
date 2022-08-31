package com.tcc.backend.sala.gruposala;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class GrupoSalaService {

    @Autowired
    private GrupoSalaRepository grupoRepository;

    public List<GrupoSala> listarGrupos() {
        return grupoRepository.findAll();
    }

    public GrupoSala criarGrupo(GrupoSala grupoRequest) {
        List<GrupoSala> listaGrupos = grupoRepository.findByNomeIgnoreCase(grupoRequest.getNome());

        if (!listaGrupos.isEmpty()) {
            throw new ResourceAlreadyExists("Grupo de Salas", "nome", grupoRequest.getNome());
        }
        return grupoRepository.save(grupoRequest);
    }

    public GrupoSala consultarGrupoPorId(Long id) {
        return grupoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("GrupoSala", id));
    }

    public GrupoSala atualizarGrupo(Long id, GrupoSala grupoRequest) {
        return null;
    }

    public void deletarGrupo(Long id) {
    }

}
