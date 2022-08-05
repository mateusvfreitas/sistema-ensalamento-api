package com.tcc.backend.curso;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.backend.exceptions.ResourceAlreadyExists;
import com.tcc.backend.exceptions.ResourceNotFoundException;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Curso criarCurso(Curso curso) {
        List<Curso> listaCursos = cursoRepository.findByNomeIgnoreCase(curso.getNome());

        if (!listaCursos.isEmpty()) {
            throw new ResourceAlreadyExists("Curso", "nome", curso.getNome());
        }

        return cursoRepository.save(curso);
    }

    public Curso consultarCursoPorId(Long id) {
        return cursoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curso", id));
    }

    public Curso atualizarCurso(Long id, Curso cursoReq) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curso", id));

        if ((!curso.getNome().equals(cursoReq.getNome()))
                && (!cursoRepository.findByNomeIgnoreCase(cursoReq.getNome()).isEmpty())) {
            throw new ResourceAlreadyExists("Curso", "nome", cursoReq.getNome());
        }
        curso.setNome(cursoReq.getNome());
        return cursoRepository.save(curso);
    }

    public void deletarCurso(Long id) {
        if (cursoRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Curso", id);
        }
        cursoRepository.deleteById(id);
    }

}
