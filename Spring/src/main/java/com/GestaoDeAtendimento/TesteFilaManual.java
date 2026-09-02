
package com.GestaoDeAtendimento;

import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.Cliente;
import com.GestaoDeAtendimento.core.service.FilaService;

import java.time.LocalDateTime;

        public class TesteFilaManual {
            public static void main(String[] args) {

                FilaService filaService = new FilaService();

                Cliente cliente1 = new Cliente(1L, "Ana", LocalDateTime.now());
                Cliente cliente2 = new Cliente(2L, "Bruno", LocalDateTime.now());
                Cliente cliente3 = new Cliente(3L, "Carla", LocalDateTime.now());

                Atendimento atendimento1 = filaService.entrarNaFila(cliente1);
                Atendimento atendimento2 = filaService.entrarNaFila(cliente2);
                Atendimento atendimento3 = filaService.entrarNaFila(cliente3);

                System.out.println("--- Clientes entraram na fila ---");
                System.out.println(atendimento1);
                System.out.println(atendimento2);
                System.out.println(atendimento3);

                System.out.println("\n--- Fila atual ---");
                System.out.println(filaService.listarFilaAtual());

                System.out.println("\n--- Chamando o próximo ---");
                Atendimento chamado = filaService.chamarProximo();
                System.out.println(chamado);

                System.out.println("\n--- Fila após chamar o próximo ---");
                System.out.println(filaService.listarFilaAtual());

                System.out.println("\n--- Finalizando o atendimento chamado ---");
                filaService.finalizarAtendimento(chamado.getNumeroSenha());
                System.out.println("Atendimento " + chamado.getNumeroSenha() + " finalizado.");

                System.out.println("\n--- Chamando o próximo novamente ---");
                Atendimento chamado2 = filaService.chamarProximo();
                System.out.println(chamado2);

                System.out.println("\n--- Cancelando esse atendimento ---");
                filaService.cancelarAtendimento(chamado2.getNumeroSenha());
                System.out.println("Atendimento " + chamado2.getNumeroSenha() + " cancelado.");

                System.out.println("\n--- Testando erro: buscar senha inexistente ---");
                try {
                    filaService.finalizarAtendimento(999L);
                } catch (Exception e) {
                    System.out.println("Erro esperado capturado: " + e.getMessage());
                }

                System.out.println("\n--- Testando erro: chamar próximo com fila vazia ---");
                try {
                    filaService.chamarProximo();
                    filaService.chamarProximo();
                } catch (Exception e) {
                    System.out.println("Erro esperado capturado: " + e.getMessage());
                }
            }
        }
