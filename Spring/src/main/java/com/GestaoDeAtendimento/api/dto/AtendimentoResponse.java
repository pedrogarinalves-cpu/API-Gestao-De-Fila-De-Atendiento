package com.GestaoDeAtendimento.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class AtendimentoResponse {

    private Long numeroSenha;
    private String nomeCliente;
    private String status;
    private LocalDateTime horarioEntrada;

    public Long getNumeroSenha() {
        return numeroSenha;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }
}
