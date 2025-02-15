package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Long> {
}
