package com.GestaoDeAtendimento.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Atendimento {

    private Long numeroSenha;
    private Cliente cliente;
    private StatusAtendimento status;
    private LocalDateTime horarioEntrada;
    private LocalDateTime horarioInicioAtendimento;
    private LocalDateTime horarioFim;

    public Atendimento(Long numeroSenha, Cliente cliente) {
        this.numeroSenha = numeroSenha;
        this.cliente = cliente;
        status = StatusAtendimento.AGUARDANDO;
        horarioEntrada = LocalDateTime.now();
    }

    public Long getNumeroSenha() {
        return numeroSenha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public StatusAtendimento getStatus() {
        return status;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public LocalDateTime getHorarioInicioAtendimento() {
        return horarioInicioAtendimento;
    }

    public LocalDateTime getHorarioFim() {
        return horarioFim;
    }

    public void iniciarAtendimento(){
        this.status = StatusAtendimento.EM_ATENDIMENTO;
        this.horarioInicioAtendimento =  LocalDateTime.now();
    }

    public void finalizar(){
        this.status = StatusAtendimento.FINALIZADO;
        this.horarioFim = LocalDateTime.now();

    }

    public void cancelar(){
        this.status = StatusAtendimento.CANCELADO;
        this.horarioFim = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Atendimento that = (Atendimento) o;
        return Objects.equals(numeroSenha, that.numeroSenha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroSenha);
    }

    @Override
    public String toString() {
        return "Atendimento{" +
                "horarioFim=" + horarioFim +
                ", horarioInicioAtendimento=" + horarioInicioAtendimento +
                ", horarioEntrada=" + horarioEntrada +
                ", status=" + status +
                ", cliente=" + cliente +
                ", numeroSenha=" + numeroSenha +
                '}';
    }
}


