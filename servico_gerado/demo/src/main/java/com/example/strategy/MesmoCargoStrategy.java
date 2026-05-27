package com.example.strategy;

import com.example.model.Cargo;
import com.example.model.Employee;
import com.example.model.Squad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MesmoCargoStrategy implements SquadStrategy {

    @Override public String getId() { return "mesmo-cargo"; }
    @Override public String getNome() { return "Mesmo Cargo"; }
    @Override public String getDescricao() {
        return "Agrupa funcionários do mesmo cargo na mesma equipe (mesmo atributo).";
    }

    @Override
    public List<Squad> recomendar(List<Employee> funcionarios, int tamanhoEquipe) {
        Map<Cargo, List<Employee>> porCargo = new LinkedHashMap<>();
        for (Employee e : funcionarios) {
            porCargo.computeIfAbsent(e.getCargo(), k -> new ArrayList<>()).add(e);
        }

        List<Squad> resultado = new ArrayList<>();
        int idx = 1;
        for (Map.Entry<Cargo, List<Employee>> entry : porCargo.entrySet()) {
            List<Squad> parciais = SquadUtils.particionar(entry.getValue(), tamanhoEquipe, "Squad " + entry.getKey());
            for (Squad s : parciais) {
                s.setNome("Squad " + (idx++) + " - " + entry.getKey());
                resultado.add(s);
            }
        }
        return resultado;
    }
}
