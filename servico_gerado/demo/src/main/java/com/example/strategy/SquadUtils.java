package com.example.strategy;

import com.example.model.Employee;
import com.example.model.Squad;

import java.util.ArrayList;
import java.util.List;

final class SquadUtils {

    private SquadUtils() {}

    static List<Squad> particionar(List<Employee> pool, int tamanhoEquipe, String prefixo) {
        List<Squad> squads = new ArrayList<>();
        if (pool.isEmpty() || tamanhoEquipe <= 0) return squads;

        int total = pool.size();
        int numEquipes = (int) Math.ceil(total / (double) tamanhoEquipe);
        for (int i = 0; i < numEquipes; i++) {
            squads.add(new Squad(prefixo + " " + (i + 1)));
        }
        for (int i = 0; i < total; i++) {
            squads.get(i / tamanhoEquipe).adicionar(pool.get(i));
        }
        return squads;
    }

    static List<Squad> distribuirRoundRobin(List<Employee> pool, int numEquipes, String prefixo) {
        List<Squad> squads = new ArrayList<>();
        if (pool.isEmpty() || numEquipes <= 0) return squads;
        for (int i = 0; i < numEquipes; i++) squads.add(new Squad(prefixo + " " + (i + 1)));
        for (int i = 0; i < pool.size(); i++) {
            squads.get(i % numEquipes).adicionar(pool.get(i));
        }
        return squads;
    }
}
