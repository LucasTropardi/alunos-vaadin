package com.lucas.services;

import com.lucas.model.Aluno;

import java.util.List;

public interface AlunoService {
    public void save(Aluno aluno);
    public void remove(Aluno aluno);
    public List<Aluno> findAll();
    public List<Aluno> find(String value);
}
