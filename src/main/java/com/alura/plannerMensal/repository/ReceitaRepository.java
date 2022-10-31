package com.alura.plannerMensal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.alura.plannerMensal.entity.Receita;

public interface ReceitaRepository extends CrudRepository<Receita, Long> {
    List<Receita> findByDescricao(String descricao);

    @Query(value = "SELECT * FROM public.receitas AS r WHERE DATE_PART('year', r.data) = :ano AND DATE_PART('month', r.data) = :mes", nativeQuery = true)
    List<Receita> findByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query(value = "SELECT SUM(receitas.valor) FROM public.receitas AS receitas WHERE DATE_PART('year', receitas.data) = :ano AND DATE_PART('month', receitas.data) = :mes", nativeQuery = true)
    Double valorTotalReceitasMes(@Param("ano") Integer ano, @Param("mes") Integer mes);
}
