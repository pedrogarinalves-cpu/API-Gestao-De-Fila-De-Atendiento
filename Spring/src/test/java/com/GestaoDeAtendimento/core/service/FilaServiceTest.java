package com.GestaoDeAtendimento.core.service;

import com.GestaoDeAtendimento.core.exception.AtendimentoNaoEncontradoException;
import com.GestaoDeAtendimento.core.exception.FilaVaziaException;
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
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
   @Test

void chamarProximoDeveLancarExceptionQuandoFilaVazia(){
        when(atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO))
                .thenReturn(List.of());
        assertThrows(FilaVaziaException.class, () -> filaService.chamarProximo());
   }

   @Test
void entrarNaFilaDeveCriarAtendimentoComStatusAguardando() {
        Cliente cliente = new Cliente(1L, "ana", LocalDateTime.now());
        Atendimento atendimentoSalvo = new Atendimento(1L, cliente);

        when(clienteRepository.save(cliente)). thenReturn(cliente);
        when(atendimentoRepository.save(any(Atendimento.class))).thenReturn(atendimentoSalvo);

       Atendimento resultado = filaService.entrarNaFila(cliente);

       assertEquals(StatusAtendimento.AGUARDANDO, resultado.getStatus());
       assertEquals(cliente, resultado.getCliente());
   }

   @Test
    void  finalizarAtendimentoDeveMudarStatusParaFinalizado() {
        Cliente cliente = new Cliente (1l, "ana", LocalDateTime.now());
        Atendimento atendimento = new Atendimento(1L, cliente);

        when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));
        when(atendimentoRepository.save(atendimento)).thenReturn(atendimento);

        filaService.finalizarAtendimento(1L);

        assertEquals(StatusAtendimento.FINALIZADO, atendimento.getStatus());
   }

   @Test
    void finalizarAtendimentoDeveLancarExceptionQuandoNaoEncontrado(){
        when(atendimentoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AtendimentoNaoEncontradoException.class, () -> filaService.finalizarAtendimento(999L));
   }
   @Test
void cancelarAtendimentoDeveMudarStatusParaCancelado() {
       Cliente cliente = new Cliente (1l, "ana", LocalDateTime.now());
       Atendimento atendimento = new Atendimento(1L, cliente);

       when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));
       when(atendimentoRepository.save(atendimento)).thenReturn(atendimento);

       filaService.cancelarAtendimento(1L);

       assertEquals(StatusAtendimento.CANCELADO, atendimento.getStatus());

   }

   @Test
void cancelarAtendimentoDeveLancarExceptionQuandoNaoEncontrado() {
       when(atendimentoRepository.findById(999L)).thenReturn(Optional.empty());

       assertThrows(AtendimentoNaoEncontradoException.class, () -> filaService.cancelarAtendimento(999L));
   }

   @Test
    void consultarPosicaoDeveRetornarIndiceCorreto() {

        Cliente cliente1 = new Cliente(1L, "jp", LocalDateTime.now());
        Cliente cliente2 = new Cliente(2L, "joao", LocalDateTime.now());

        Atendimento atendimento1 = new Atendimento(1L, cliente1);
        Atendimento atendimento2 = new Atendimento(2L, cliente2);

        when(atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO))
                .thenReturn(List.of(atendimento1, atendimento2));

        int posicao = filaService.consultarPosicao(2L);


         assertEquals(1, posicao);
   }

   @Test
    void consultarPosicaoDeveLancarExceptionQuandoNaoEncontrado() {

        when(atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO))
                .thenReturn(List.of());

       assertThrows(AtendimentoNaoEncontradoException.class, () -> filaService.consultarPosicao(999L));
   }

   @Test
   void  listarFilaAtualDeveRetornarTodosOsAtendimentosAguardando(){

        Cliente cliente = new Cliente (1L, "marcio", LocalDateTime.now());
        Atendimento atendimento = new Atendimento(1L, cliente);

        when(atendimentoRepository.findByStatusOrderByHorarioEntradaAsc(StatusAtendimento.AGUARDANDO))
                .thenReturn(List.of(atendimento));

       List<Atendimento> resultado = filaService.listarFilaAtual();

       assertEquals(1, resultado.size());
       assertEquals(atendimento, resultado.get(0));
   }
}
