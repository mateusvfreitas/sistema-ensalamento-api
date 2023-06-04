package com.tcc.backend.blocoaula;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.enums.EnDiaSemana;
import com.tcc.backend.exceptions.ResourceNotFoundException;
import com.tcc.backend.horarioaula.HorarioAula;
import com.tcc.backend.horarioaula.HorarioAulaRepository;
import com.tcc.backend.sala.atributosala.AtributoSala;

@Service
public class BlocoAulaService {

    @Autowired
    private BlocoAulaRepository blocoAulaRepository;

    @Autowired
    private HorarioAulaRepository horarioAulaRepository;

    public List<BlocoAula> listarTodos() {
        return blocoAulaRepository.findAll();
    }

    public List<BlocoAula> listarAulasFiltros(String responsavel, List<AtributoSala> filtroAtributos,
            EnDiaSemana diaSemana, Boolean isMatchAll) {
        if (Boolean.TRUE.equals(isMatchAll) && filtroAtributos != null) {
            Long qtdeAtributos = Long.valueOf(filtroAtributos.size());
            if (responsavel != null) {
                return blocoAulaRepository.findByFiltrosAndUsuarioMatchAll(responsavel, filtroAtributos,
                        diaSemana,
                        qtdeAtributos);
            } else {
                return blocoAulaRepository.findByFiltrosMatchAll(filtroAtributos, diaSemana, qtdeAtributos);
            }
        }
        return blocoAulaRepository.findByUsuarioResponsavelGeneric(responsavel, filtroAtributos, diaSemana);
    }

    public BlocoAula criarBlocoAula(BlocoAula blocoAula) {
        return blocoAulaRepository.save(blocoAula);
    }

    public BlocoAula consultarBlocoAulaPorId(Long id) {
        return blocoAulaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aula"));
    }

    public BlocoAula atualizarBlocoAula(Long id, BlocoAula blocoAulaReq) {
        BlocoAula blocoAula = blocoAulaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aula"));

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
            throw new ResourceNotFoundException("Aula");
        }
        blocoAulaRepository.deleteById(id);
    }

    // public List<BlocoAula> findByUsuarioResponsavel(String responsavel) {
    // return blocoAulaRepository.findByUsuarioResponsavel(responsavel);
    // }

    public List<HeatMapDto> gerarHeatMap(List<AtributoSala> listaAtributos) {
        List<HorarioAula> horarios = horarioAulaRepository.findAllByOrderByHorarioInicio();
        List<BlocoAula> aulas;

        if (listaAtributos == null) {
            aulas = blocoAulaRepository.findAll();
        } else {
            Long qtdeAtributos = Long.valueOf(listaAtributos.size());
            aulas = blocoAulaRepository.findByFiltrosMatchAll(listaAtributos, null, qtdeAtributos);
        }

        List<HeatMapDto> heatMap = new ArrayList<>();

        horarios.forEach(horario -> {
            HeatMapDto heatMapHorario = new HeatMapDto();
            List<HeatMapDetalhe> listaDetalhes = new ArrayList<>();
            heatMapHorario.setNomeHorario(horario.getNome());
            int soma = 0;
            for (EnDiaSemana dia : EnDiaSemana.values()) {
                HeatMapDetalhe detalhe = new HeatMapDetalhe();
                detalhe.setDiaSemana(dia.getDescricao());
                aulas.forEach(aula -> {
                    if (aula.getDiaSemana() == dia
                            && horario.getHorarioFim().compareTo(aula.getHorarioInicio().getHorarioInicio()) > 0
                            && horario.getHorarioInicio().compareTo(aula.getHorarioFim().getHorarioFim()) < 0) {
                        detalhe.contagem += 1;
                    }
                });
                listaDetalhes.add(detalhe);
                soma += detalhe.getContagem();
            }
            heatMapHorario.setListaDetalhes(listaDetalhes);
            heatMapHorario.setMedia((float) soma / 5);
            heatMap.add(heatMapHorario);
        });
        return heatMap;
    }
}
