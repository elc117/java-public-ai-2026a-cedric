package com.example.controller;

import com.example.dao.EmployeeDAO;
import com.example.model.Employee;
import com.example.model.Squad;
import com.example.strategy.SquadStrategy;
import com.example.strategy.StrategyFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SquadController {

    private final EmployeeDAO dao = new EmployeeDAO();

    public void registrar(Javalin app) {
        app.get("/api/strategies", this::listarEstrategias);
        app.post("/api/squads/recomendar", this::recomendar);
    }

    private void listarEstrategias(Context ctx) {
        List<Map<String, String>> lista = StrategyFactory.listar().stream()
                .map(s -> Map.of(
                        "id", s.getId(),
                        "nome", s.getNome(),
                        "descricao", s.getDescricao()))
                .toList();
        ctx.json(lista);
    }

    private void recomendar(Context ctx) {
        @SuppressWarnings("unchecked")
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String strategyId = String.valueOf(body.getOrDefault("strategy", ""));
        SquadStrategy strategy = StrategyFactory.get(strategyId).orElse(null);
        if (strategy == null) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Estratégia desconhecida: " + strategyId);
            return;
        }

        List<Employee> funcionarios = dao.listar();
        if (funcionarios.isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Não há funcionários cadastrados");
            return;
        }

        int tamanho = tamanhoEquipe(body, funcionarios.size());
        if (tamanho <= 0) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Informe tamanhoEquipe ou numeroEquipes (> 0)");
            return;
        }

        List<Squad> squads = strategy.recomendar(funcionarios, tamanho);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("strategy", Map.of("id", strategy.getId(), "nome", strategy.getNome()));
        resposta.put("tamanhoEquipe", tamanho);
        resposta.put("squads", squads);
        ctx.json(resposta);
    }

    private int tamanhoEquipe(Map<String, Object> body, int totalFuncionarios) {
        Object t = body.get("tamanhoEquipe");
        if (t instanceof Number n && n.intValue() > 0) return n.intValue();

        Object num = body.get("numeroEquipes");
        if (num instanceof Number n && n.intValue() > 0) {
            return (int) Math.ceil(totalFuncionarios / (double) n.intValue());
        }
        return 0;
    }
}
