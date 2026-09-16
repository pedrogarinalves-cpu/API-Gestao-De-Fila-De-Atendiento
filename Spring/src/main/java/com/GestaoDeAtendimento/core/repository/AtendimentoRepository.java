package com.GestaoDeAtendimento.core.repository;

import com.GestaoDeAtendimento.core.model.Atendimento;
import com.GestaoDeAtendimento.core.model.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByStatusOrderByhorarioentradaAsc(StatusAtendimento status);
}
