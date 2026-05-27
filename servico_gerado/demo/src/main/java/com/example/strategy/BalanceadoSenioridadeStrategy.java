package com.example.strategy;

import com.example.model.Employee;
import com.example.model.Squad;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BalanceadoSenioridadeStrategy implements SquadStrategy {

    @Override public String getId() { return "balanceado-senioridade"; }
    @Override public String getNome() { return "Balanceado por Senioridade"; }
    @Override public String getDescricao() {
        return "Distribui sêniores, plenos e juniores de forma equilibrada entre as equipes (atributo balanceado).";
    }

    @Override
    public List<Squad> recomendar(List<Employee> funcionarios, int tamanhoEquipe) {
        List<Employee> ordenados = new ArrayList<>(funcionarios);
        ordenados.sort(Comparator.comparingInt((Employee e) -> e.getSenioridade().getNivel()).reversed());

        int numEquipes = Math.max(1, (int) Math.ceil(ordenados.size() / (double) tamanhoEquipe));
        return SquadUtils.distribuirRoundRobin(ordenados, numEquipes, "Squad");
    }
}
