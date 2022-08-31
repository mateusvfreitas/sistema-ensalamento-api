package com.tcc.backend.sala;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public List<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    public Sala criarSala(Sala sala) {
        List<Sala> listaSalas = salaRepository.findByNomeIgnoreCase(sala.getNome());

        if (!listaSalas.isEmpty()) {
            throw new ResourceAlreadyExists("Sala", "nome", sala.getNome());
        }

        return salaRepository.save(sala);
    }

    public Sala consultarSalaPorId(Long id) {
        return salaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sala", id));
    }

    public Sala atualizarSala(Long id, Sala salaReq) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala", id));
        if ((!sala.getNome().equals(salaReq.getNome()))
                && (!salaRepository.findByNomeIgnoreCase(salaReq.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("Sala", "nome", salaReq.getNome());
        }
        sala.setNome(salaReq.getNome());
        sala.setCapacidade(salaReq.getCapacidade());
        sala.setAtributos(salaReq.getAtributos());
        sala.setIsExclusiva(salaReq.getIsExclusiva());
        sala.setIsLiberar(salaReq.getIsLiberar());
        sala.setGrupoSala(salaReq.getGrupoSala());
        return salaRepository.save(sala);
    }

    public void deletarSala(Long id) {
        salaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sala", id));

        // sala.getBlocoAula().forEach(blocoAula.getSala().remove(sala));

        salaRepository.deleteById(id);
    }
}
