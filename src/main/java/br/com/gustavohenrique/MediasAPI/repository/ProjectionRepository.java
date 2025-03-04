package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Long> {

    List<Projection> findByCourse(Course course);

    Optional<Projection> findByCourseAndId(Course course, Long id);

    @Modifying
    @Query(value = "DELETE FROM projection WHERE course_id = :courseId",nativeQuery = true)
    void deleteAllByCourse(@Param("courseId") Long courseId);

    boolean existsByCourseAndName(Course course, String name);

    @Modifying
    @Query(value = "DELETE FROM projection WHERE id = :id;",nativeQuery = true)
    void deleteProjection(@Param("id") Long id);

}