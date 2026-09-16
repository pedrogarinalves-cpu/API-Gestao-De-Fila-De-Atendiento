package com.GestaoDeAtendimento.core.repository;

import com.GestaoDeAtendimento.core.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
