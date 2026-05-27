package com.example.strategy;

import com.example.model.Employee;
import com.example.model.Squad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MesmaEspecialidadeStrategy implements SquadStrategy {

    @Override public String getId() { return "mesma-especialidade"; }
    @Override public String getNome() { return "Mesma Especialidade"; }
    @Override public String getDescricao() {
        return "Agrupa funcionários pela mesma especialidade — útil para squads focadas em uma stack ou área.";
    }

    @Override
    public List<Squad> recomendar(List<Employee> funcionarios, int tamanhoEquipe) {
        Map<String, List<Employee>> porEsp = new LinkedHashMap<>();
        for (Employee e : funcionarios) {
            porEsp.computeIfAbsent(e.getEspecialidade(), k -> new ArrayList<>()).add(e);
        }

        List<Squad> resultado = new ArrayList<>();
        int idx = 1;
        for (Map.Entry<String, List<Employee>> entry : porEsp.entrySet()) {
            List<Squad> parciais = SquadUtils.particionar(entry.getValue(), tamanhoEquipe, "");
            for (Squad s : parciais) {
                s.setNome("Squad " + (idx++) + " - " + entry.getKey());
                resultado.add(s);
            }
        }
        return resultado;
    }
}
