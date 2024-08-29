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
    
    @Size(max=16)
    @Column(length=16)
    private Double[] atividades;
    
    @Size(max=2)
    @Column(length=2)
    private Double[] provas;
    
    public Double[] getAtividades() {
        return atividades;
    }


    public void setAtividades(Double[] atividades) {
        this.atividades = atividades;
    }


    public Double[] getProvas() {
        return provas;
    }


    public void setProvas(Double[] provas) {
        this.provas = provas;
    }

    private Double media; 
    
    public Alunos() {
    }
    

    public Alunos(
            @NotNull(groups = CriarAluno.class) @NotEmpty(groups = CriarAluno.class) @Size(groups = CriarAluno.class, min = 1, max = 100) String nome,
            Integer matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    
    public String getNome() {
        return nome;
    }

   

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
        result = prime * result + ((media == null) ? 0 : media.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alunos other = (Alunos) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (matricula == null) {
            if (other.matricula != null)
                return false;
        } else if (!matricula.equals(other.matricula))
            return false;
        if (media == null) {
            if (other.media != null)
                return false;
        } else if (!media.equals(other.media))
            return false;
        return true;
    }
}
