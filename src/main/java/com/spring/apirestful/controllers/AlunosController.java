package com.spring.apirestful.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.apirestful.models.Alunos;
import com.spring.apirestful.models.Alunos.CriarAluno;
import com.spring.apirestful.services.AlunosService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/aluno")
@Validated
public class AlunosController {

    @Autowired
    private AlunosService alunosService;

    @GetMapping("/{id}")
    public ResponseEntity<Alunos> findById (@PathVariable Long id) {
        Alunos aluno = this.alunosService.findById(id);
        return ResponseEntity.ok().body(aluno);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Alunos>> findAll () {
        List<Alunos> aluno = this.alunosService.findAll();
        return ResponseEntity.ok().body(aluno);
    }
    
    @PostMapping
    @Validated(CriarAluno.class)
    public ResponseEntity<Void> create(@Valid @RequestBody Alunos aluno) {
        this.alunosService.createAluno(aluno);
        URI hateos = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(aluno.getId()).toUri();
        
        return ResponseEntity.created(hateos).build();
    }
    
}
