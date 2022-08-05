package com.tcc.backend.horarioaula;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class HorarioAulaService {

    @Autowired
    private HorarioAulaRepository horarioAulaRepository;

    public List<HorarioAula> listarTodos() {
        return horarioAulaRepository.findAll();
    }

    public HorarioAula criarHorarioAula(HorarioAula horarioAula) {
        List<HorarioAula> listaHorarios = horarioAulaRepository.findByNomeIgnoreCase(horarioAula.getNome());

        if (!listaHorarios.isEmpty()) {
            throw new ResourceAlreadyExists("Horario", "nome", horarioAula.getNome());
        }

        return horarioAulaRepository.save(horarioAula);
    }

    public HorarioAula consultarHorarioPorId(Long id) {
        return horarioAulaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Horario", id));
    }

    public HorarioAula atualizarHorarioAula(Long id, HorarioAula horarioAulaReq) {
        HorarioAula horarioAula = horarioAulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario", id));

        if ((!horarioAula.getNome().equals(horarioAulaReq.getNome()))
                && (!horarioAulaRepository.findByNomeIgnoreCase(horarioAulaReq.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("Horario", "nome", horarioAulaReq.getNome());
        }
        horarioAula.setNome(horarioAulaReq.getNome());
        return horarioAulaRepository.save(horarioAula);
    }

    public void deletarHorarioAula(Long id) {
        if (horarioAulaRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Horario", id);
        }
        horarioAulaRepository.deleteById(id);
    }
}
