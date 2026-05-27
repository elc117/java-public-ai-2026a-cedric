package com.example.dao;

import com.example.db.Database;
import com.example.model.Cargo;
import com.example.model.Employee;
import com.example.model.Senioridade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {

    public List<Employee> listar() {
        List<Employee> lista = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY id";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Optional<Employee> buscar(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Employee criar(Employee e) {
        String sql = "INSERT INTO employees (nome, cargo, senioridade, especialidade, horas_disponiveis) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getCargo().name());
            ps.setString(3, e.getSenioridade().name());
            ps.setString(4, e.getEspecialidade());
            ps.setInt(5, e.getHorasDisponiveis());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }

    public Optional<Employee> atualizar(int id, Employee e) {
        String sql = "UPDATE employees SET nome=?, cargo=?, senioridade=?, especialidade=?, horas_disponiveis=? WHERE id=?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getCargo().name());
            ps.setString(3, e.getSenioridade().name());
            ps.setString(4, e.getEspecialidade());
            ps.setInt(5, e.getHorasDisponiveis());
            ps.setInt(6, id);
            int n = ps.executeUpdate();
            if (n == 0) return Optional.empty();
            e.setId(id);
            return Optional.of(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Employee map(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("nome"),
                Cargo.valueOf(rs.getString("cargo")),
                Senioridade.valueOf(rs.getString("senioridade")),
                rs.getString("especialidade"),
                rs.getInt("horas_disponiveis")
        );
    }
}
