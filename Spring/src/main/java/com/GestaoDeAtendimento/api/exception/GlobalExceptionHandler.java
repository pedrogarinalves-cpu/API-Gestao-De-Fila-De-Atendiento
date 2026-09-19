package com.GestaoDeAtendimento.api.exception;

import com.GestaoDeAtendimento.core.exception.AtendimentoNaoEncontradoException;
import com.GestaoDeAtendimento.core.exception.FilaVaziaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FilaVaziaException.class)
    public ResponseEntity<String> tratarFilaVazia(FilaVaziaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AtendimentoNaoEncontradoException.class)
    public ResponseEntity<String> tratarAtendimentoNaoEncontrado(AtendimentoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
