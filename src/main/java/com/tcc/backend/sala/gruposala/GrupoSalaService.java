package com.tcc.backend.sala.gruposala;

import java.util.ArrayList;
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
        return grupoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agrupamento"));
    }

    public GrupoSala atualizarGrupo(Long id, GrupoSala grupoRequest) {
        GrupoSala grupoSala = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupamento"));

        if ((!grupoSala.getNome().equals(grupoRequest.getNome()))
                && (!grupoRepository.findByNomeIgnoreCase(grupoRequest.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("Agrupamento", "nome", grupoRequest.getNome());
        }
        grupoSala.setNome(grupoRequest.getNome());

        return grupoRepository.save(grupoSala);
    }

    public void deletarGrupo(Long id) {
    }

    public List<GrupoSala> buscarGruposPorId(List<Long> listaIds) {
        List<GrupoSala> grupoSalas = new ArrayList<>();
        listaIds.forEach(el -> {
            GrupoSala grupoSala = grupoRepository.findById(el)
                    .orElseThrow(() -> new ResourceNotFoundException("Agrupamento"));
            grupoSalas.add(grupoSala);
        });

        return grupoSalas;
    }

}
