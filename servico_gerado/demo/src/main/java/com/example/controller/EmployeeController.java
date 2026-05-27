package com.example.controller;

import com.example.dao.EmployeeDAO;
import com.example.model.Cargo;
import com.example.model.Employee;
import com.example.model.Senioridade;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class EmployeeController {

    private final EmployeeDAO dao = new EmployeeDAO();

    public void registrar(Javalin app) {
        app.get("/api/employees", this::listar);
        app.get("/api/employees/{id}", this::buscar);
        app.post("/api/employees", this::criar);
        app.put("/api/employees/{id}", this::atualizar);
        app.delete("/api/employees/{id}", this::remover);

        app.get("/api/cargos", ctx -> ctx.json(Cargo.values()));
        app.get("/api/senioridades", ctx -> ctx.json(Senioridade.values()));
    }

    private void listar(Context ctx) {
        ctx.json(dao.listar());
    }

    private void buscar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        dao.buscar(id).ifPresentOrElse(
                ctx::json,
                () -> ctx.status(HttpStatus.NOT_FOUND).result("Funcionário não encontrado")
        );
    }

    private void criar(Context ctx) {
        Employee e = ctx.bodyAsClass(Employee.class);
        ctx.status(HttpStatus.CREATED).json(dao.criar(e));
    }

    private void atualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Employee e = ctx.bodyAsClass(Employee.class);
        dao.atualizar(id, e).ifPresentOrElse(
                ctx::json,
                () -> ctx.status(HttpStatus.NOT_FOUND).result("Funcionário não encontrado")
        );
    }

    private void remover(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (dao.remover(id)) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND).result("Funcionário não encontrado");
        }
    }
}
