package com.spring.apirestful.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.apirestful.models.Alunos;
import com.spring.apirestful.services.AlunosService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/sgbd")
public class SgbdController {

    @Autowired
    private AlunosService alunosService;

     // Mostrar a nota de um aluno em especifico
    @GetMapping("/{id}")
    public ResponseEntity<Alunos> findById (@PathVariable Integer matricula) {
        Alunos aluno = this.alunosService.findById(matricula);
        return ResponseEntity.ok().body(aluno);
    }
    
    // Mostrar todos os alunos e notas 
    @GetMapping("/all")
    public ResponseEntity<List<Alunos>> findAll () {
        List<Alunos> aluno = this.alunosService.findAll();
        return ResponseEntity.ok().body(aluno);
    }
    
    // Cadastrar um aluno
    @PostMapping
    @Validated
    public ResponseEntity<Alunos> create(@Valid @RequestBody Alunos aluno) {
        this.alunosService.createAluno(aluno);
        URI hateos = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(aluno.getMatricula()).toUri();
        
        // return ResponseEntity.created(hateos).build();
        return ResponseEntity.ok().body(aluno);
    }

    // Excluir um aluno 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer matricula){
        this.alunosService.deleteAluno(matricula);
        return ResponseEntity.ok().build();
    }
    
    //  Inserir todas as notas de uma só vez
    @PostMapping("/notas")
    @Validated
    public  ResponseEntity<Alunos> InserirNotas(@Valid @RequestBody Alunos aluno) {
        Alunos newAluno = this.alunosService.inserirNotas(aluno);
        return ResponseEntity.ok().body(newAluno);
    }
    

    // settar notas atividades em uma posição especifica
    // settar notas provas 
    // add notas atividades
    // add notas provas 
    // get aprovados 
    // get reprovados 
    // Download CSV 


    // --------- nao funcionais _--- 

    // estudar ResponseEntity 
    // HATEOS 
    // Junit 
    // Swagger 
    // Jwt 
    // Security
    // clean arq 
}
