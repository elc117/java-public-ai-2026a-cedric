package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Squad {

    private String nome;
    private List<Employee> membros;

    public Squad() {
        this.membros = new ArrayList<>();
    }

    public Squad(String nome) {
        this.nome = nome;
        this.membros = new ArrayList<>();
    }

    public Squad(String nome, List<Employee> membros) {
        this.nome = nome;
        this.membros = membros;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Employee> getMembros() { return membros; }
    public void setMembros(List<Employee> membros) { this.membros = membros; }

    public void adicionar(Employee e) { this.membros.add(e); }

    public int getTamanho() { return membros.size(); }
}
