package com.lucas.services;

import com.lucas.model.Aluno;
import com.lucas.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlunoServiceImpl implements AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    @Transactional
    public void save(Aluno aluno) {
        alunoRepository.save(aluno);
    }

    @Override
    @Transactional
    public void remove(Aluno aluno) {
        alunoRepository.delete(aluno);
    }

    @Override
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    @Override
    public List<Aluno> find(String substring) {
        return alunoRepository.findAluno(substring);
    }
}
