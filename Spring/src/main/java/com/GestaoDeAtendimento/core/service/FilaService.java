package com.GestaoDeAtendimento.core.service;

import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.Cliente;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FilaService {

     Queue<Atendimento> fila = new LinkedList<>();
     private GeradorDeSenha geradorDeSenha;
     private Map<Long, Atendimento> atendimentoEmAndamento;

    public FilaService(Queue<Atendimento> fila, GeradorDeSenha geradorDeSenha, Map<Long, Atendimento> atendimentoEmAndamento) {
        this.fila = fila;
        this.geradorDeSenha = geradorDeSenha;
        this.atendimentoEmAndamento = atendimentoEmAndamento;
    }
    public Atendimento entarNaFila(Cliente cliente){
        Long numeroSenha = geradorDeSenha.proximaSenha();

         Atendimento atendimento = new Atendimento( numeroSenha,  cliente );

        fila.add(atendimento);
        return atendimento;
    }
}
