package com.GestaoDeAtendimento.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PosicaoResponse {


    private int posicao;

    public int getPosicao() {
        return posicao;
    }
}
