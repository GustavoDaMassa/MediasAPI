package com.spring.apirestful.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.apirestful.models.Alunos;

@Repository
public interface AlunosRepository extends JpaRepository<Alunos, Long> {
    
}
