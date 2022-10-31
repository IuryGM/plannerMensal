package com.alura.plannerMensal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.alura.plannerMensal.entity.Despesa;

public interface DespesaRepository extends CrudRepository<Despesa, Long> {
    List<Despesa> findByDescricao(String descricao);

    @Query(value = "SELECT * FROM public.despesas AS despesas WHERE DATE_PART('year', despesas.data) = :ano AND DATE_PART('month', despesas.data) = :mes", nativeQuery = true)
    List<Despesa> findByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

}
