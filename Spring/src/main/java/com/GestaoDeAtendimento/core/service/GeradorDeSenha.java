package com.GestaoDeAtendimento.core.service;

public class GeradorDeSenha {
    private Long contador = 0L;

    public Long proximaSenha() {
        contador++;
        return contador;
    }
}
