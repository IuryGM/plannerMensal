package com.alura.plannerMensal.controller.dto;

public class DespesaCategoriaDTO {
    private Integer id;
    private String tipo;
    private Double valor_total;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor_total() {
        return this.valor_total;
    }

    public void setValor_total(Double valor_total) {
        this.valor_total = valor_total;
    }
}
