package com.tcc.backend.sala.atributosala;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class AtributoSalaService {

    @Autowired
    private AtributoSalaRepository atributoSalaRepository;

    public List<AtributoSala> listarAtributos() {
        return atributoSalaRepository.findAll();
    }

    public AtributoSala criarAtributoSala(AtributoSala atributoSala) {
        List<AtributoSala> listaAtributoSalasExistentes = atributoSalaRepository.findByNomeIgnoreCase(atributoSala.getNome());

        if (!listaAtributoSalasExistentes.isEmpty()) {
            throw new ResourceAlreadyExists("Atributo", "nome", atributoSala.getNome());
        }

        return atributoSalaRepository.save(atributoSala);
    }

    public List<AtributoSala> criarListaAtributoSala(List<AtributoSala> listaAtributoSala) {
        List<AtributoSala> atributosCriados = new ArrayList<>();

        listaAtributoSala.forEach(atributo -> {
            AtributoSala atributoSala = criarAtributoSala(atributo);
            atributosCriados.add(atributoSala);
        });

        return atributosCriados;
    }

    public AtributoSala consultarAtributoSalaPorId(Long id) {
        return atributoSalaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Atributo", id));
    }

    public AtributoSala atualizarAtributoSala(Long id, AtributoSala atributoSalaReq) {
        AtributoSala atributoSala = atributoSalaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AtributoSala", id));

        if ((!atributoSala.getNome().equals(atributoSalaReq.getNome()))
                && (!atributoSalaRepository.findByNomeIgnoreCase(atributoSalaReq.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("AtributoSala", "nome", atributoSalaReq.getNome());
        }
        atributoSala.setNome(atributoSalaReq.getNome());
        return atributoSalaRepository.save(atributoSala);
    }

    public void deletarAtributoSala(Long id) {
        AtributoSala atributo = atributoSalaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atributo", id));

        atributo.getSalas().forEach(sala -> sala.getAtributos().remove(atributo));

        atributoSalaRepository.deleteById(id);
    }
}
