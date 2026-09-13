package com.GestaoDeAtendimento.api.controller;

import com.GestaoDeAtendimento.api.dto.AtendimentoResponse;
import com.GestaoDeAtendimento.api.dto.EntrarNaFilaRequest;
import com.GestaoDeAtendimento.api.dto.PosicaoResponse;
import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.Cliente;
import com.GestaoDeAtendimento.core.service.FilaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/fila")
public class FilaController {

    private final FilaService filaService;

    public FilaController(FilaService filaService) {
        this.filaService = filaService;
    }

    @PostMapping
    public AtendimentoResponse entrarNaFila (@RequestBody EntrarNaFilaRequest request){
        Cliente cliente = new Cliente(null, request.getNome(), LocalDateTime.now());
        Atendimento atendimento = filaService.entrarNaFila(cliente);
        return converterParaResponse(atendimento);
    }

    @PostMapping("/proximo")
    public AtendimentoResponse chamarProximo (){
        Atendimento atendimento = filaService.chamarProximo();
        return converterParaResponse(atendimento);
    }

    @PutMapping("/{numeroSenha}/finalizar")
    public void finalizarAtendimento(@PathVariable long numeroSenha){
        filaService.finalizarAtendimento(numeroSenha);
    }

    @DeleteMapping ("/{numeroSenha}")
    public void cancelarAtendimento(@PathVariable long numeroSenha){
        filaService.cancelarAtendimento(numeroSenha);
    }

    @GetMapping("/{numeroSenha}/posicao")
    public PosicaoResponse consultarPosicao(@PathVariable long numeroSenha){
        int posicao = filaService.consultarPosicao(numeroSenha);
        return new PosicaoResponse(posicao);
    }

    @GetMapping
    public List <AtendimentoResponse> listarFilaAtual(){
        return filaService.listarFilaAtual()
                .stream()
                .map(this:: converterParaResponse)
                .collect(Collectors.toList());
    }


    private AtendimentoResponse converterParaResponse(Atendimento atendimento) {
        return new AtendimentoResponse(
                atendimento.getNumeroSenha(),
                atendimento.getCliente().getNome(),
                atendimento.getStatus().name(),
                atendimento.getHorarioEntrada()
        );
    }
}
