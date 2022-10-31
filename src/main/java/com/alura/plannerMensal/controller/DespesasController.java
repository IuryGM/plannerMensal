package com.alura.plannerMensal.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alura.plannerMensal.controller.dto.DespesaDTO;
import com.alura.plannerMensal.entity.Despesa;
import com.alura.plannerMensal.repository.DespesaRepository;

@RestController
public class DespesasController {

    @Autowired
    DespesaRepository despesaRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/despesas")
    ResponseEntity<?> getAll(@RequestParam Optional<String> descricao) {
        try {
            List<Despesa> despesas;

            if (descricao.isPresent())
                despesas = (List<Despesa>) despesaRepository.findByDescricao(descricao.get());
            else
                despesas = (List<Despesa>) despesaRepository.findAll();

            return ResponseEntity.ok().body(this.listEntityToDto(despesas));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/despesas/{id}")
    ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            Optional<Despesa> despesa = (despesaRepository.findById(id));
            if (despesa.isPresent()) {
                DespesaDTO despesaDTO = this.entityToDTO(despesa.get());
                return ResponseEntity.ok().body(despesaDTO);
            } else {
                return ResponseEntity.ok().body("Despesa não existe");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/despesas/{ano}/{mes}")
    ResponseEntity<?> getByYearAndMonth(@PathVariable("ano") Integer ano, @PathVariable("mes") Integer mes){
        try {
            List<Despesa> despesas = despesaRepository.findByYearAndMonth(ano, mes);
            return ResponseEntity.ok().body(despesas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/despesas")
    ResponseEntity<?> addDespesa(@Valid @RequestBody DespesaDTO despesa) {
        try {
            despesaRepository.save(this.dtoToEntity(despesa));
            return ResponseEntity.ok().body("Dados adicionados");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/despesas/{id}")
    ResponseEntity<?> updateDespesa(@Valid @RequestBody DespesaDTO despesaDTO, @PathVariable("id") Long id) {
        try {
            return despesaRepository.findById(id).map(record -> {
                record.setDescricao(despesaDTO.getDescricao());
                record.setData(despesaDTO.getData());
                record.setValor(despesaDTO.getValor());
                record.setId_categoria(despesaDTO.getId_categoria());
                despesaRepository.save(record);
                DespesaDTO updated = this.entityToDTO(record);
                return ResponseEntity.ok().body(updated);
            }).orElse(ResponseEntity.notFound().build());
        } catch (HttpMessageConversionException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/despesas/{id}")
    ResponseEntity<?> deleteDespesa(@PathVariable("id") Long id) {
        try {
            despesaRepository.deleteById(id);
            return ResponseEntity.ok().body("");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public Despesa dtoToEntity(DespesaDTO despesa) {
        Despesa entity = modelMapper.map(despesa, Despesa.class);
        return entity;
    }

    public DespesaDTO entityToDTO(Despesa despesa) {
        DespesaDTO dto = modelMapper.map(despesa, DespesaDTO.class);
        return dto;
    }

    public List<Despesa> listDtoToEntity(List<DespesaDTO> listDespesa) {
        return listDespesa.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

    public List<DespesaDTO> listEntityToDto(List<Despesa> listDespesa) {
        return listDespesa.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

}
