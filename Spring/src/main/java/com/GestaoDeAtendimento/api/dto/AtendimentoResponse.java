package com.GestaoDeAtendimento.api.dto;

import java.time.LocalDateTime;

public class AtendimentoResponse {

    private Long numeroSenha;
    private String nomeCliente;
    private String status;
    private LocalDateTime horarioEntrada;

    public AtendimentoResponse(Long numeroSenha, String nomeCliente, String status, LocalDateTime horarioEntrada) {
        this.numeroSenha = numeroSenha;
        this.nomeCliente = nomeCliente;
        this.status = status;
        this.horarioEntrada = horarioEntrada;
    }

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
