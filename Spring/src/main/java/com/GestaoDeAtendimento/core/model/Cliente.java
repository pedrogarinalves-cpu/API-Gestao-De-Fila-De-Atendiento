package com.GestaoDeAtendimento.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cliente {

    private final Long id;
    private final String nome;
    private final LocalDateTime horarioChegada;

    public Cliente(Long id, String nome, LocalDateTime horarioChegada) {
        this.id = id;
        this.nome = nome;
        this.horarioChegada = horarioChegada;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getHoarioChegada() {
        return horarioChegada;
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", horarioChegada=" + horarioChegada +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
