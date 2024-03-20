package com.lucas.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    @NotEmpty(message = "Iforme o nome.")
    private String nome;
    @Column
    @Min(value = 4, message = "A idade deve ser maior ou igual a 4.")
    @Max(value = 99, message = "A idade máxima suportada é 99.")
    @NotNull(message = "Informe a idade.")
    private int idade;
    @Column
    @Min(value = 10000000, message = "Iforme um CEP maior ou igual a 10000000")
    @Max(value = 99999999, message = "Iforme um CEP menor ou igual a 99999999")
    @NotNull(message = "Iforme o CEP.")
    @Digits(integer = 8, fraction = 0, message = "O CEP deve conter 8 dígitos")
    private int cep;
    @Column
    @NotEmpty(message = "Informe a nacionalidade.")
    private String nacionalidade;
    @ManyToOne
    @JoinColumn
    private Status status;

    public Aluno() {

    }

    public Aluno(String nome, int idade, int cep, String nacionalidade, Status status) {
        this.nome = nome;
        this.idade = idade;
        this.cep = cep;
        this.nacionalidade = nacionalidade;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
