package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:squads.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        String createSql = """
                CREATE TABLE IF NOT EXISTS employees (
                    id                INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome              TEXT    NOT NULL,
                    cargo             TEXT    NOT NULL,
                    senioridade       TEXT    NOT NULL,
                    especialidade     TEXT    NOT NULL,
                    horas_disponiveis INTEGER NOT NULL
                );
                """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            ensureSchema(conn, stmt);
            stmt.execute(createSql);
            seedSeIVazio(stmt);
            System.out.println("Banco de dados inicializado.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar banco de dados", e);
        }
    }

    private static void ensureSchema(Connection conn, Statement stmt) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, "employees", "especialidade")) {
            if (!rs.next()) {
                stmt.execute("DROP TABLE IF EXISTS employees");
            }
        }
    }

    private static void seedSeIVazio(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM employees")) {
            if (rs.next() && rs.getInt(1) > 0) return;
        }
        String[] seeds = {
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Ana Souza',      'BACKEND',  'SENIOR', 'Java',       40)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Bruno Lima',    'BACKEND',  'PLENO',  'Node.js',    30)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Carla Dias',    'FRONTEND', 'JUNIOR', 'React',      20)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Diego Reis',    'FRONTEND', 'SENIOR', 'React',      40)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Elisa Costa',   'DADOS',    'PLENO',  'Python',     30)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Felipe Nunes',  'DADOS',    'SENIOR', 'SQL',        40)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Gabi Martins',  'DESIGN',   'JUNIOR', 'UI/UX',      15)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Hugo Alves',    'DESIGN',   'PLENO',  'UI/UX',      30)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Iara Pinto',    'QA',       'PLENO',  'Automação',  25)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Joao Rocha',    'QA',       'JUNIOR', 'Manual',     20)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Karen Melo',    'PO',       'SENIOR', 'Agile',      40)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Lucas Faria',   'BACKEND',  'JUNIOR', 'Java',       20)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Marina Cruz',   'FRONTEND', 'PLENO',  'Vue.js',     30)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Nina Teixeira', 'DADOS',    'JUNIOR', 'Python',     15)",
                "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES ('Otavio Prado',  'BACKEND',  'PLENO',  'Java',       35)"
        };
        for (String sql : seeds) stmt.execute(sql);
    }
}
