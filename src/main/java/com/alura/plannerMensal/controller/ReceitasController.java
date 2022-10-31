package com.alura.plannerMensal.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alura.plannerMensal.controller.dto.ReceitaDTO;
import com.alura.plannerMensal.entity.Receita;
import com.alura.plannerMensal.repository.ReceitaRepository;

@RestController
public class ReceitasController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/receitas/{ano}/{mes}")
    ResponseEntity<?> getReceita(@PathVariable("ano") Integer ano, @PathVariable("mes") Integer mes) {
        try {
            List<Receita> receitas = receitaRepository.findByYearAndMonth(ano, mes);
            List<ReceitaDTO> receitasDTO = this.listEntityToDto(receitas);
            return ResponseEntity.ok().body(receitasDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/receitas/{id}")
    ResponseEntity<?> getReceita(@PathVariable("id") Long id) {
        try {
            Optional<Receita> receita = (receitaRepository.findById(id));
            if (receita.isPresent()) {
                ReceitaDTO receitaDTO = this.entityToDTO(receita.get());
                return new ResponseEntity<>(receitaDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Receita não existente", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/receitas")
    ResponseEntity<?> getAll(@RequestParam Optional<String> descricao) {
        try {
            List<Receita> receitas;
            if (descricao.isPresent())
                receitas = (List<Receita>) receitaRepository.findByDescricao(descricao.get());
            else
                receitas = (List<Receita>) receitaRepository.findAll();
            return ResponseEntity.ok().body(this.listEntityToDto(receitas));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }    

    @PostMapping("/receitas")
    ResponseEntity<?> addReceita(@Valid @RequestBody ReceitaDTO receitaDTO) {
        try {
            receitaRepository.save(this.dtoToEntity(receitaDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/receitas/{id}")
    ResponseEntity<?> updateReceita(@Valid @RequestBody ReceitaDTO receitaDTO, @PathVariable("id") Long id) {
        try {
            return receitaRepository.findById(id).map(record -> {
                record.setDescricao(receitaDTO.getDescricao());
                record.setData(receitaDTO.getData());
                record.setValor(receitaDTO.getValor());
                record.setId_categoria(receitaDTO.getId_categoria());
                receitaRepository.save(record);
                ReceitaDTO updated = this.entityToDTO(record);
                return ResponseEntity.ok().body(updated);
            }).orElse(ResponseEntity.notFound().build());
        } catch (HttpMessageConversionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/receitas/{id}")
    ResponseEntity<?> deleteReceita(@PathVariable("id") Long id) {
        try {
            receitaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Receita dtoToEntity(ReceitaDTO receita) {
        Receita entity = modelMapper.map(receita, Receita.class);
        return entity;
    }

    public ReceitaDTO entityToDTO(Receita receita) {
        ReceitaDTO dto = modelMapper.map(receita, ReceitaDTO.class);
        return dto;
    }

    public List<Receita> listDtoToEntity(List<ReceitaDTO> listaReceita) {
        return listaReceita.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

    public List<ReceitaDTO> listEntityToDto(List<Receita> listaReceita) {
        return listaReceita.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

}
