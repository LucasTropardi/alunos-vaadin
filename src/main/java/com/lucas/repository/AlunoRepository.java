package com.lucas.repository;

import com.lucas.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

    @Query("select s from alunos s where lower(s.nome) like lower(concat('%', :substring, '%'))")
    List<Aluno> findAluno(String substring);
}
