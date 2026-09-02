package com.GestaoDeAtendimento.core.service;

import com.GestaoDeAtendimento.core.exception.AtendimentoNaoEncontradoException;
import com.GestaoDeAtendimento.core.exception.FilaVaziaException;
import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.Cliente;

import java.util.*;

public class FilaService {

     Queue<Atendimento> fila = new LinkedList<>();
     private GeradorDeSenha geradorDeSenha;
     private Map<Long, Atendimento> atendimentoEmAndamento;

    public FilaService() {
        this.fila = new LinkedList<>();
        this.geradorDeSenha =  new GeradorDeSenha();
        this.atendimentoEmAndamento = new HashMap<>();
    }

    public Atendimento entrarNaFila(Cliente cliente){
        Long numeroSenha = geradorDeSenha.proximaSenha();

         Atendimento atendimento = new Atendimento( numeroSenha,  cliente );

        fila.add(atendimento);
        return atendimento;
    }

    public Atendimento chamarProximo(){
       if (fila.isEmpty()){
           throw new FilaVaziaException("Não há atendimentos aguardando na fila");
    }
        Atendimento atendimento = fila.poll();
        atendimento.iniciarAtendimento();
        atendimentoEmAndamento.put(atendimento.getNumeroSenha(), atendimento);

        return atendimento;
    }
    public void finalizarAtendimento(Long numeroSenha) {
        Atendimento atendimento = atendimentoEmAndamento.get(numeroSenha);

        if (atendimento == null) {
            throw new AtendimentoNaoEncontradoException("Atendimento com senha " + numeroSenha + " não encontrado");
        }

        atendimento.finalizar();
        atendimentoEmAndamento.remove(numeroSenha);
    }
    public void cancelarAtendimento(Long numeroSenha) {
        Atendimento atendimento = atendimentoEmAndamento.get(numeroSenha);

        if (atendimento != null) {
            atendimento.cancelar();
            atendimentoEmAndamento.remove(numeroSenha);
            return;
        }

        for (Atendimento a : fila) {
            if (a.getNumeroSenha().equals(numeroSenha)) {
                a.cancelar();
                fila.remove(a);
                return;
            }
        }

        throw new AtendimentoNaoEncontradoException("Atendimento com senha " + numeroSenha + " não encontrado");
    }
    public int consultarPosicao(Long numeroSenha) {
        int posicao = 0;

        for (Atendimento a : fila) {
            if (a.getNumeroSenha().equals(numeroSenha)) {
                return posicao;
            }
            posicao++;
        }

        throw new AtendimentoNaoEncontradoException("Atendimento com senha " + numeroSenha + " não encontrado");
    }
    public List<Atendimento> listarFilaAtual() {
        return new ArrayList<>(fila);
    }
}
