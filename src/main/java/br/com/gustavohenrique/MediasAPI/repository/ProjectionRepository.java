package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Long> {

    List<Projection> findByCourseId(Course course);

    Optional<Projection> findByCourseAndId(Course course, Long id);

    boolean existsByCourseAndId(Course course, Long id);

    void deleteAllByCourse(Course course);

    boolean existsByCourseAndName(Course course, String name);

}