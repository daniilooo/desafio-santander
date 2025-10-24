package com.santander.desafio.repository;

import com.santander.desafio.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
}
