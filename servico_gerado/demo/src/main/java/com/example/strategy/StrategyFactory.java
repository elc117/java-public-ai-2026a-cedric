package com.example.strategy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StrategyFactory {

    private static final Map<String, SquadStrategy> ESTRATEGIAS = new LinkedHashMap<>();

    static {
        registrar(new AleatorioStrategy());
        registrar(new MesmoCargoStrategy());
        registrar(new MesmaEspecialidadeStrategy());
        registrar(new BalanceadoSenioridadeStrategy());
        registrar(new DisponibilidadeStrategy());
    }

    private static void registrar(SquadStrategy s) {
        ESTRATEGIAS.put(s.getId(), s);
    }

    public static Optional<SquadStrategy> get(String id) {
        return Optional.ofNullable(ESTRATEGIAS.get(id));
    }

    public static List<SquadStrategy> listar() {
        return List.copyOf(ESTRATEGIAS.values());
    }
}
