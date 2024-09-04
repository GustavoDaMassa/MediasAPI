package com.spring.apirestful.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.apirestful.models.Alunos;
import com.spring.apirestful.repositories.AlunosRepository;

@Service
public class AlunosService {
    
    private AlunosRepository alunosrepository;
    
    public Alunos findById(Integer id){

        Optional<Alunos> aluno = this.alunosrepository.findById(id);

        return aluno.orElseThrow(() -> new RuntimeException("Aluno não encontrado! Id:" + id +"."));
    }
    
    public List<Alunos> findAll(){

        List<Alunos> aluno = this.alunosrepository.findAll();

        return aluno;
    }

    @Transactional
    public Alunos createAluno(Alunos aluno){

        aluno = this.alunosrepository.save(aluno);
        return aluno;
    }

    @Transactional
    public Alunos UpdateAlunos(Alunos aluno){
        Alunos newAluno = findById(aluno.getId());
        newAluno.setAtividades(aluno.getAtividades());
        return this.alunosrepository.save(newAluno);
    }

    public void deleteAluno(Integer id){
        findById(id);
        try {
            this.alunosrepository.deleteById(id);       
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
