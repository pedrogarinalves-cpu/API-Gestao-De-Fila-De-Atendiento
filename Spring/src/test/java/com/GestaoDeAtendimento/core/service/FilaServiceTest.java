package com.GestaoDeAtendimento.core.service;

import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.Cliente;
import com.GestaoDeAtendimento.core.model.StatusAtendimento;
import com.GestaoDeAtendimento.core.repository.AtendimentoRepository;
import com.GestaoDeAtendimento.core.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilaServiceTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private FilaService filaService;


    @Test
    void chamarProximo() {

        Cliente cliente = new Cliente (1L, "ana", LocalDateTime.now());
        Atendimento atendimento = new Atendimento(1L, cliente);

        when(atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO))
                .thenReturn(List.of(atendimento));

        when(atendimentoRepository.save(atendimento)).thenReturn(atendimento);

        Atendimento resultado = filaService.chamarProximo();

        assertEquals(StatusAtendimento.EM_ATENDIMENTO, resultado.getStatus());
        assertEquals(cliente, resultado.getCliente());
    }
}