package com.GestaoDeAtendimento.api.dto;

import jakarta.validation.constraints.NotBlank;

public class EntrarNaFilaRequest {


@NotBlank (message = "o nome é obirgatório")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
