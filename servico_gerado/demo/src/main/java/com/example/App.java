package com.example;

import com.example.controller.EmployeeController;
import com.example.controller.SquadController;
import com.example.db.Database;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class App {

    public static void main(String[] args) {
        Database.init();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(3000);

        new EmployeeController().registrar(app);
        new SquadController().registrar(app);

        System.out.println("Servidor rodando em http://localhost:3000");
    }
}
