package com.GestaoDeAtendimento.api.dto;

public class PosicaoResponse {
    private int posicao;

    public PosicaoResponse (int posicao) {
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }
}
