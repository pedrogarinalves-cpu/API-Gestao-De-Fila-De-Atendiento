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

}
