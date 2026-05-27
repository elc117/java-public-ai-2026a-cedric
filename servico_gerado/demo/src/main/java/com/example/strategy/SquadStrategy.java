package com.example.strategy;

import com.example.model.Employee;
import com.example.model.Squad;

import java.util.List;

public interface SquadStrategy {

    String getId();

    String getNome();

    String getDescricao();

    List<Squad> recomendar(List<Employee> funcionarios, int tamanhoEquipe);
}
