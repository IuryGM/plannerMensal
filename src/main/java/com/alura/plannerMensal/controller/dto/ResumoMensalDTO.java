package com.alura.plannerMensal.controller.dto;

import java.util.List;

public class ResumoMensalDTO {
    private Double totalReceitas;
    private Double totalDespesas;
    private Double saldo;
    private List<DespesaCategoriaDTO> despesasCategoria;

    public Double getTotalReceitas() {
        return this.totalReceitas;
    }

    public void setTotalReceitas(Double totalReceitas) {
        this.totalReceitas = totalReceitas;
    }

    public Double getTotalDespesas() {
        return this.totalDespesas;
    }

    public void setTotalDespesas(Double totalDespesas) {
        this.totalDespesas = totalDespesas;
    }

    public Double getSaldo() {
        return this.saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public List<DespesaCategoriaDTO> getDespesasCategoria() {
        return this.despesasCategoria;
    }

    public void setDespesasCategoria(List<DespesaCategoriaDTO> despesasCategoria) {
        this.despesasCategoria = despesasCategoria;
    }
    
}
