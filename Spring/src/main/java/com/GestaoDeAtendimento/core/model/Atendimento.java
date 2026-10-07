package com.GestaoDeAtendimento.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Include
    private Long numeroSenha;

   @ManyToOne
    private Cliente cliente;

   @Enumerated(EnumType.STRING)
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

}


