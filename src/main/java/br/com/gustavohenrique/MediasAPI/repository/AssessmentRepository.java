package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByProjection(Projection projectionId);

    @Query(value = "SELECT * FROM assessment  WHERE identifier = :identifier AND projection_id = :projection",nativeQuery = true)
    Assessment findByIndentifier(@Param("identifier") String identifier,@Param("projection") Long projection);

    @Query(value = "SELECT MAX(max_value) FROM assessment WHERE projection_id = :projection",nativeQuery = true)
    Double getBiggerMaxValue(@Param("projection") Long projection);

    Optional<Assessment> findByProjectionIdAndId(Long projectionId, Long id);
}
