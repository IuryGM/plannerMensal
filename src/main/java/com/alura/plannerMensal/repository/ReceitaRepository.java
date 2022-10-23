package com.alura.plannerMensal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.alura.plannerMensal.entity.Receita;

public interface ReceitaRepository extends CrudRepository<Receita, Long> {
    List<Receita> findByDescricao(String descricao);
}
