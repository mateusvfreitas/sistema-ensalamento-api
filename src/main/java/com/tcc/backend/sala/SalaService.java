package com.tcc.backend.sala;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.blocoaula.BlocoAula;
import com.tcc.backend.blocoaula.BlocoAulaRepository;
import com.tcc.backend.ensalamento.EnsalamentoAlteracaoDto;
import com.tcc.backend.ensalamento.EnsalamentoDetalheDto;
import com.tcc.backend.ensalamento.EnsalamentoDto;
import com.tcc.backend.enums.EnDiaSemana;
import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;
import com.tcc.backend.horarioaula.HorarioAula;
import com.tcc.backend.horarioaula.HorarioAulaRepository;
import com.tcc.backend.sala.atributosala.AtributoSala;
import com.tcc.backend.sala.atributosala.AtributoSalaRepository;
import com.tcc.backend.sala.gruposala.GrupoSala;

import ilog.concert.IloException;
import ilog.opl.IloCplex;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplSettings;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private AtributoSalaRepository atributoSalaRepository;

    @Autowired
    private BlocoAulaRepository blocoAulaRepository;

    @Autowired
    private HorarioAulaRepository horarioAulaRepository;

    public List<Sala> listarSalas() {
        return salaRepository.findAll();
        // return salaRepository.findByFiltros(filtroAtributos);
    }

    public List<Sala> listarSalasFiltros(List<AtributoSala> filtroAtributos, Integer filtroCapacidade,
            List<GrupoSala> filtroGrupos, Boolean isMatchAll) {
        // return salaRepository.findAll();
        if (Boolean.TRUE.equals(isMatchAll) && filtroAtributos != null) {
            Long qtdeAtributos = Long.valueOf(filtroAtributos.size());
            return salaRepository.findByFiltrosMatchAll(filtroAtributos, filtroCapacidade, filtroGrupos, qtdeAtributos);
        }
        return salaRepository.findByFiltrosGeneric(filtroAtributos, filtroCapacidade, filtroGrupos);
    }

    public Sala criarSala(Sala sala) {
        List<Sala> listaSalas = salaRepository.findByNomeIgnoreCase(sala.getNome());

        if (!listaSalas.isEmpty()) {
            throw new ResourceAlreadyExists("Sala", "nome", sala.getNome());
        }

        return salaRepository.save(sala);
    }

    public Sala consultarSalaPorId(Long id) {
        return salaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sala"));
    }

    public Sala atualizarSala(Long id, Sala salaReq) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala"));
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
        salaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sala"));

        // sala.getBlocoAula().forEach(blocoAula.getSala().remove(sala));

        salaRepository.deleteById(id);
    }

    public List<EnsalamentoDto> consultaEnsalamentoEspecifico(Sala sala) {
        // Sala salaFiltrada = salaRepository.findById(idSala).orElseThrow(() -> new
        // ResourceNotFoundException("Sala"));
        List<HorarioAula> horarios = horarioAulaRepository.findAllByOrderByHorarioInicio();
        List<BlocoAula> aulas = blocoAulaRepository.findBySalaAtual(sala);

        List<EnsalamentoDto> ensalamento = new ArrayList<>();

        horarios.forEach(horario -> {
            EnsalamentoDto ensalamentoHorario = new EnsalamentoDto();
            List<EnsalamentoDetalheDto> listaEnsalamentoDetalhes = new ArrayList<>();

            ensalamentoHorario.setNomeHorario(horario.getNome());

            for (EnDiaSemana dia : EnDiaSemana.values()) {
                EnsalamentoDetalheDto detalhe = new EnsalamentoDetalheDto();
                detalhe.setDiaSemana(dia.getDescricao());
                aulas.forEach(aula -> {
                    if (aula.getDiaSemana() == dia
                            && horario.getHorarioFim().compareTo(aula.getHorarioInicio().getHorarioInicio()) > 0
                            && horario.getHorarioInicio().compareTo(aula.getHorarioFim().getHorarioFim()) < 0) {
                        detalhe.setCodigoDisciplina(aula.getDisciplina());
                        detalhe.setTurma(aula.getTurma());
                    }
                });
                listaEnsalamentoDetalhes.add(detalhe);
            }
            ensalamentoHorario.setListaDetalhes(listaEnsalamentoDetalhes);
            ensalamento.add(ensalamentoHorario);
        });
        return ensalamento;
    }

    public List<EnsalamentoAlteracaoDto> consultaAlteracoesEnsalamento(List<GrupoSala> filtroGrupos) {
        geraArquivoDat(filtroGrupos);

        gerarEnsalamento();

        return getEnsalamento();
        // Thread thread = new Thread(() -> {
        // });

        // thread.start();

        // try {
        // thread.join();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

    }

    public void geraArquivoDat(List<GrupoSala> filtroGrupos) {
        try {
            FileWriter myWriter = new FileWriter("src\\main\\resources\\cplex\\alocacao.dat");

            formataSalas(myWriter, filtroGrupos);

            formataAtributos(myWriter);

            formataDisciplinas(myWriter, filtroGrupos);

            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarEnsalamento() {
        IloOplFactory.setDebugMode(true);
        IloOplFactory oplF = new IloOplFactory();
        IloOplErrorHandler errHandler = oplF.createOplErrorHandler();
        IloOplModelSource modelSource = oplF.createOplModelSource(
                "C:\\Users\\mateu\\OneDrive\\Documentos\\GitHub\\sistema-ensalamento-api\\src\\main\\resources\\cplex\\alocacao.mod");
        IloOplSettings settings = oplF.createOplSettings(errHandler);
        IloOplModelDefinition def = oplF.createOplModelDefinition(modelSource, settings);
        IloCplex cplex = null;
        try {
            cplex = oplF.createCplex();
        } catch (IloException e) {
            e.printStackTrace();
        }
        cplex.setOut(null);
        IloOplModel opl = oplF.createOplModel(def, cplex);
        IloOplDataSource dataSource = oplF.createOplDataSource(
                "C:\\Users\\mateu\\OneDrive\\Documentos\\GitHub\\sistema-ensalamento-api\\src\\main\\resources\\cplex\\alocacao.dat");
        opl.addDataSource(dataSource);
        opl.generate();
        try {
            if (cplex.solve()) {
                System.out.println("OBJECTIVE: " + opl.getCplex().getObjValue());
                opl.postProcess();
                // opl.printSolution(System.out);
            } else {
                System.out.println("No solution!");
            }
        } catch (IloException e) {
            e.printStackTrace();
        }
        oplF.end();

        // System.exit(status);
    }

    public List<EnsalamentoAlteracaoDto> getEnsalamento() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<EnsalamentoAlteracaoDto> listaAlteracoes = new ArrayList<>();

        try {
            List<EnsalamentoAlteracaoDto> listaJsonAlteracao = objectMapper.readValue(
                    new File("src\\main\\resources\\cplex\\alteracoes.json"),
                    new TypeReference<List<EnsalamentoAlteracaoDto>>() {
                    });

            listaAlteracoes = listaJsonAlteracao;
        } catch (IOException e) {
            EnsalamentoAlteracaoDto objetoCasoUnfeasible = new EnsalamentoAlteracaoDto();
            objetoCasoUnfeasible.setCodigo("ERRO");
            listaAlteracoes.add(objetoCasoUnfeasible);
            e.printStackTrace();
            return listaAlteracoes;
        }
        return listaAlteracoes;
    }

    private void formataSalas(FileWriter myWriter, List<GrupoSala> filtroGrupos) throws IOException {
        List<Sala> salas = salaRepository.findByGrupoSalaIn(filtroGrupos);

        myWriter.write("l_salas = {\n");
        salas.forEach(sala -> {
            String stringDados = "<\"" + sala.getNome() + "\", " + sala.getCapacidade() + ">,\n";
            try {
                myWriter.write(stringDados);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myWriter.write("};\n\n");

        myWriter.write("l_salas_exclusivas = {\n");
        salas.forEach(sala -> {
            if (Boolean.TRUE.equals(sala.getIsExclusiva())) {
                String stringDados = "\"" + sala.getNome() + "\",\n";
                try {
                    myWriter.write(stringDados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        myWriter.write("};\n\n");

        myWriter.write("l_salas_minimo_uso = {\n");
        salas.forEach(sala -> {
            if (Boolean.TRUE.equals(sala.getIsLiberar())) {
                String stringDados = "\"" + sala.getNome() + "\",\n";
                try {
                    myWriter.write(stringDados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        myWriter.write("};\n\n");

        myWriter.write("l_salas_atributos = {\n");
        salas.forEach(sala -> {
            sala.getAtributos().forEach(atributo -> {
                String stringDados = "<\"" + sala.getNome() + "\", \"" + atributo.getNome() + "\">,\n";
                try {
                    myWriter.write(stringDados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        myWriter.write("};\n\n");
    }

    private void formataAtributos(FileWriter myWriter) throws IOException {
        List<AtributoSala> atributoSalas = atributoSalaRepository.findAll();

        myWriter.write("l_atributos = {\n");
        atributoSalas.forEach(atributo -> {
            String stringDados = "\"" + atributo.getNome() + "\",\n";
            try {
                myWriter.write(stringDados);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myWriter.write("};\n\n");
    }

    private void formataDisciplinas(FileWriter myWriter, List<GrupoSala> filtroGrupos) throws IOException {
        List<BlocoAula> disciplinas = blocoAulaRepository.findAll();
        List<HorarioAula> horariosOrdenados = horarioAulaRepository.findAllByOrderByHorarioInicio();

        myWriter.write("l_salas_especificas = {\n");
        disciplinas.forEach(disciplina -> {
            if (disciplina.getSalaEspecifica() != null
                    && filtroGrupos.contains(disciplina.getSalaEspecifica().getGrupoSala())) {
                String stringDados = "<\"" + disciplina.getSalaEspecifica().getNome()
                        + "\", \""
                        + disciplina.getDisciplina()
                        + "-" + disciplina.getTurma() +
                        "\">,\n";
                try {
                    myWriter.write(stringDados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        myWriter.write("};\n\n");

        myWriter.write("l_disciplinas_atributos = {\n");
        disciplinas.forEach(disciplina -> {
            disciplina.getAtributosSala().forEach(atributo -> {
                String stringDados = "<\"" + disciplina.getDisciplina() + "-" + disciplina.getTurma() + "\", \""
                        + atributo.getNome() +
                        "\">,\n";
                try {
                    myWriter.write(stringDados);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        });
        myWriter.write("};\n\n");

        myWriter.write("l_disciplinas = {\n");
        disciplinas.forEach(disciplina -> {
            String stringDados = "<\"" + disciplina.getDisciplina() + "-" + disciplina.getTurma() + "\", "
                    + disciplina.getDiaSemana().ordinal()
                    + ", "
                    + (horariosOrdenados.indexOf(disciplina.getHorarioInicio()) + 1) + ", "
                    + (horariosOrdenados.indexOf(disciplina.getHorarioFim()) + 1) + ">,\n";
            try {
                myWriter.write(stringDados);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        myWriter.write("};\n\n");

        myWriter.write("l_alocacao_atual = {\n");
        disciplinas.forEach(disciplina -> {
            if (disciplina.getSalaAtual() != null && filtroGrupos.contains(disciplina.getSalaAtual().getGrupoSala())) {
                String stringDados = "<\"" + disciplina.getDisciplina() + "-" + disciplina.getTurma() + "\", "
                        + disciplina.getDiaSemana().ordinal()
                        + ", "
                        + (horariosOrdenados.indexOf(disciplina.getHorarioInicio()) + 1) + ", "
                        + (horariosOrdenados.indexOf(disciplina.getHorarioFim()) + 1) + ", \""
                        + disciplina.getSalaAtual().getNome() + "\">,\n";
                try {
                    myWriter.write(stringDados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        myWriter.write("};\n\n");
    }
}
