package com.alura.plannerMensal.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.alura.plannerMensal.controller.dto.DespesaCategoriaDTO;
import com.alura.plannerMensal.controller.dto.ResumoMensalDTO;
import com.alura.plannerMensal.entity.DespesaCategoria;
import com.alura.plannerMensal.repository.DespesaCategoriaRepository;
import com.alura.plannerMensal.repository.ReceitaRepository;

@RestController
public class ResumoController {
    
    @Autowired
    ReceitaRepository receitaRepository;

    @Autowired
    DespesaCategoriaRepository despesaRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/resumo/{ano}/{mes}")
    ResponseEntity<?> getResumoMes(@PathVariable("ano") Integer ano, @PathVariable("mes") Integer mes){
        try {
            Double valorTotalReceitas = receitaRepository.valorTotalReceitasMes(ano, mes);
            List<DespesaCategoria> despesasCategorias = despesaRepository.groupExpensesByCategory(ano, mes);
            List<DespesaCategoriaDTO> despesasCategoriaDTO = this.listEntityToDTO(despesasCategorias);

            ResumoMensalDTO resumo = this.gerarResumoMensalDTO(valorTotalReceitas, despesasCategoriaDTO);

            return ResponseEntity.ok().body(resumo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private ResumoMensalDTO gerarResumoMensalDTO(Double valorTotalReceitas, List<DespesaCategoriaDTO> despesasCategoriaDTO) {
        ResumoMensalDTO resumo = new ResumoMensalDTO();

        Double valorTotalDespesas = 0.0;

        if (valorTotalReceitas == null)
            valorTotalReceitas = 0.0;

        if (!despesasCategoriaDTO.isEmpty())
            for (DespesaCategoriaDTO despesa : despesasCategoriaDTO)
                valorTotalDespesas += despesa.getValor_total();

        resumo.setTotalReceitas(valorTotalReceitas);
        resumo.setTotalDespesas(valorTotalDespesas);
        resumo.setDespesasCategoria(despesasCategoriaDTO);
        resumo.setSaldo(valorTotalReceitas-valorTotalDespesas);

        return resumo;
    }

    public DespesaCategoriaDTO entityToDTO(DespesaCategoria receita) {
        DespesaCategoriaDTO dto = modelMapper.map(receita, DespesaCategoriaDTO.class);
        return dto;
    }

    public List<DespesaCategoriaDTO> listEntityToDTO(List<DespesaCategoria> listaReceita) {
        return listaReceita.stream().map(this::entityToDTO).collect(Collectors.toList());
    }    

}
