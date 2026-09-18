package com.GestaoDeAtendimento.core.service;

import com.GestaoDeAtendimento.core.exception.AtendimentoNaoEncontradoException;
import com.GestaoDeAtendimento.core.exception.FilaVaziaException;
import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.Cliente;
import com.GestaoDeAtendimento.core.model.StatusAtendimento;
import com.GestaoDeAtendimento.core.repository.AtendimentoRepository;
import com.GestaoDeAtendimento.core.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.*;
@Service

public class FilaService {

    private AtendimentoRepository atendimentoRepository;
    private ClienteRepository clienteRepository;

    public FilaService(AtendimentoRepository atendimentoRepository,ClienteRepository clienteRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.clienteRepository = clienteRepository;
    }

    public Atendimento entrarNaFila(Cliente cliente) {

        clienteRepository.save(cliente);
        Atendimento atendimento = new Atendimento(null, cliente);
        return atendimentoRepository.save(atendimento);

    }

    public Atendimento chamarProximo() {
        List<Atendimento> aguardando = atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO);
        if (aguardando.isEmpty()) {
            throw new FilaVaziaException("Não há atendimentos aguardando na fila");
        }
        Atendimento atendimento = aguardando.get(0);
        atendimento.iniciarAtendimento();
        return atendimentoRepository.save(atendimento);
    }

    public void finalizarAtendimento(Long numeroSenha) {
        Atendimento atendimento = atendimentoRepository.findById(numeroSenha)
                .orElseThrow(() -> new AtendimentoNaoEncontradoException("Atendimento com senha " + numeroSenha + " não encontrado"));
        atendimento.finalizar();
        atendimentoRepository.save(atendimento);
    }

    public void cancelarAtendimento(Long numeroSenha) {
        Atendimento atendimento = atendimentoRepository.findById(numeroSenha)
                .orElseThrow(() -> new AtendimentoNaoEncontradoException("Atendimento com senha " + numeroSenha + " não encontrado"));

        atendimento.cancelar();
        atendimentoRepository.save(atendimento);
    }

    public int consultarPosicao(Long numeroSenha) {
        List<Atendimento> aguardando = atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO);

        for (int i = 0; i < aguardando.size(); i++) {
            if (aguardando.get(i).getNumeroSenha().equals(numeroSenha)) {
                return i;
            }


        }
        throw new AtendimentoNaoEncontradoException("Atendimento com senha " + numeroSenha + " não encontrado");
    }

    public List<Atendimento> listarFilaAtual() {
        return atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO);
    }
}