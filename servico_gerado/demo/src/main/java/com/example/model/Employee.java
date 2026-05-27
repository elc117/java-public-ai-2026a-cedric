package com.example.model;

public class Employee {

    private int id;
    private String nome;
    private Cargo cargo;
    private Senioridade senioridade;
    private String especialidade;
    private int horasDisponiveis;

    public Employee() {}

    public Employee(int id, String nome, Cargo cargo, Senioridade senioridade,
                    String especialidade, int horasDisponiveis) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.senioridade = senioridade;
        this.especialidade = especialidade;
        this.horasDisponiveis = horasDisponiveis;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public Senioridade getSenioridade() { return senioridade; }
    public void setSenioridade(Senioridade senioridade) { this.senioridade = senioridade; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public int getHorasDisponiveis() { return horasDisponiveis; }
    public void setHorasDisponiveis(int horasDisponiveis) { this.horasDisponiveis = horasDisponiveis; }

    @Override
    public String toString() {
        return nome + " (" + cargo + " / " + senioridade + ")";
    }
}
