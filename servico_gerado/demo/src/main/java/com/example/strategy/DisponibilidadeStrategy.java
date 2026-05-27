package com.example.strategy;

import com.example.model.Employee;
import com.example.model.Squad;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisponibilidadeStrategy implements SquadStrategy {

    @Override public String getId() { return "disponibilidade"; }
    @Override public String getNome() { return "Por Disponibilidade"; }
    @Override public String getDescricao() {
        return "Agrupa funcionários com horas semanais semelhantes — squads full-time juntas, part-time juntas.";
    }

    @Override
    public List<Squad> recomendar(List<Employee> funcionarios, int tamanhoEquipe) {
        List<Employee> ordenados = new ArrayList<>(funcionarios);
        ordenados.sort(Comparator.comparingInt(Employee::getHorasDisponiveis).reversed());

        List<Squad> squads = SquadUtils.particionar(ordenados, tamanhoEquipe, "Squad");
        for (int i = 0; i < squads.size(); i++) {
            squads.get(i).setNome("Squad " + (i + 1) + " - " + rotuloDisponibilidade(squads.get(i)));
        }
        return squads;
    }

    private String rotuloDisponibilidade(Squad s) {
        double media = s.getMembros().stream().mapToInt(Employee::getHorasDisponiveis).average().orElse(0);
        if (media >= 35) return "Full-time";
        if (media >= 20) return "Part-time";
        return "Reduzido";
    }
}
