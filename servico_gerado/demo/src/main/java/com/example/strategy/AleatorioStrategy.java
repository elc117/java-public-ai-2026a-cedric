package com.example.strategy;

import com.example.model.Employee;
import com.example.model.Squad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AleatorioStrategy implements SquadStrategy {

    @Override public String getId() { return "aleatorio"; }
    @Override public String getNome() { return "Aleatório"; }
    @Override public String getDescricao() {
        return "Distribui os funcionários em equipes de forma totalmente aleatória.";
    }

    @Override
    public List<Squad> recomendar(List<Employee> funcionarios, int tamanhoEquipe) {
        List<Employee> pool = new ArrayList<>(funcionarios);
        Collections.shuffle(pool);
        return SquadUtils.particionar(pool, tamanhoEquipe, "Squad");
    }
}
