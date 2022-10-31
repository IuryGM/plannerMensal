package com.alura.plannerMensal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.alura.plannerMensal.entity.DespesaCategoria;

public interface DespesaCategoriaRepository extends CrudRepository<DespesaCategoria, Long>{
    
    @Query(value = 
    "SELECT " + 
    "   categorias.id, " + 
    "   categorias.tipo, " + 
    "   grupos_categoria.valor_total " +
    "FROM " +
    "	public.categoria AS categorias, " +
    "	( " + 
    "		SELECT " +
    "	 		SUM(despesas.valor) AS valor_total, despesas.id_categoria " +
    "	 	FROM " + 
    "	 		public.despesas AS despesas " +
    "		WHERE " +
    "			DATE_PART('year', despesas.data) = :ano AND DATE_PART('month', despesas.data) = :mes " +
    "		GROUP BY " + 
    "	 		despesas.id_categoria " +
    "	) AS grupos_categoria " +
    "WHERE grupos_categoria.id_categoria = categorias.id"
    , nativeQuery = true)
    List<DespesaCategoria> groupExpensesByCategory(@Param("ano") Integer ano, @Param("mes") Integer mes);
}
