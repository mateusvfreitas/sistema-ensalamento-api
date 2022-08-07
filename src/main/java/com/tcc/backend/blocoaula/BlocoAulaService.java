package com.tcc.backend.blocoaula;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class BlocoAulaService {
    
    @Autowired
    private BlocoAulaRepository blocoAulaRepository;

    public List<BlocoAula> listarTodos() {
        return blocoAulaRepository.findAll();
    }

    public BlocoAula criarBlocoAula(BlocoAula blocoAula) {
        return blocoAulaRepository.save(blocoAula);
    }

    public BlocoAula consultarBlocoAulaPorId(Long id) {
        return blocoAulaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BlocoAula", id));
    }

    public BlocoAula atualizarBlocoAula(Long id, BlocoAula blocoAulaReq) {
        BlocoAula blocoAula = blocoAulaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BlocoAula", id));

        blocoAula.setCurso(blocoAulaReq.getCurso());
        blocoAula.setDisciplina(blocoAulaReq.getDisciplina());
        blocoAula.setTurma(blocoAulaReq.getTurma());
        blocoAula.setDiaSemana(blocoAulaReq.getDiaSemana());
        blocoAula.setHorarioInicio(blocoAulaReq.getHorarioInicio());
        blocoAula.setHorarioFim(blocoAulaReq.getHorarioFim());
        blocoAula.setAtributosSala(blocoAulaReq.getAtributosSala());
        blocoAula.setSalaEspecifica(blocoAulaReq.getSalaEspecifica());
        blocoAula.setSalaAtual(blocoAulaReq.getSalaAtual());
        return blocoAulaRepository.save(blocoAula);
    }

    public void deletarBlocoAula(Long id) {
        if (blocoAulaRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("BlocoAula", id);
        }
        blocoAulaRepository.deleteById(id);
    }
}
