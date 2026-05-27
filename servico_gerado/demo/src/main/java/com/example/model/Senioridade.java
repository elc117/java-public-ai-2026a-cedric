package com.example.model;

public enum Senioridade {
    JUNIOR(1),
    PLENO(2),
    SENIOR(3);

    private final int nivel;

    Senioridade(int nivel) { this.nivel = nivel; }

    public int getNivel() { return nivel; }
}
