package com.alura.plannerMensal.repository;

import org.springframework.data.repository.CrudRepository;

import com.alura.plannerMensal.entity.Despesa;

public interface DespesaRepository extends CrudRepository<Despesa, Long> {
}
