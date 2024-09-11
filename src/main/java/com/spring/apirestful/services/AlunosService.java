package com.spring.apirestful.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.apirestful.models.Alunos;
import com.spring.apirestful.repositories.AlunosRepository;

@Service
public class AlunosService {
    
    @Autowired
    private AlunosRepository alunosrepository;
    
    // mostrar aluno em especifico 
    public Alunos findById(Integer matricula){
        Optional<Alunos> aluno = this.alunosrepository.findById(matricula);

        return aluno.orElseThrow(() -> new RuntimeException("Aluno não encontrado! Id:" + matricula +"."));
    }
    
    // Mostrar todos os alunos 
    public List<Alunos> findAll(){
        List<Alunos> aluno = this.alunosrepository.findAll();

        return aluno;
    }

    // Cadastrar aluno 
    @Transactional
    public Alunos createAluno(Alunos aluno){

        aluno.setMedia(alunosrepository.CalcularMedia(aluno));
        aluno = this.alunosrepository.save(aluno);
        return aluno;
    }

    // Inserir notas em forma de uma lista 
    @Transactional
    public Alunos inserirNotas(Alunos aluno){
        Alunos newAluno = findById(aluno.getMatricula());
        newAluno.setAtividades(aluno.getAtividades());
        newAluno.setProvas(aluno.getProvas());
        aluno.setMedia(alunosrepository.CalcularMedia(aluno));
        return this.alunosrepository.save(newAluno);
    }

    // Excluir um aluno 
    public void deleteAluno(Integer id){
        findById(id);
        try {
            this.alunosrepository.deleteById(id);       
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
