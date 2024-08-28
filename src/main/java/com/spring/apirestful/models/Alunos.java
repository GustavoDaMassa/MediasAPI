package com.spring.apirestful.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@Entity
@Table(name="alunos")
public class Alunos {

    public interface CriarAluno {

    }
   

    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable=false, length=100)
    @NotNull(groups= CriarAluno.class)
    @NotEmpty(groups= CriarAluno.class)
    @Size(groups= CriarAluno.class, min=1, max=100)
    private String nome;

    @Column(length=9, nullable=false, unique=true)
    private Integer matricula;

    private Double media; 


    
}
