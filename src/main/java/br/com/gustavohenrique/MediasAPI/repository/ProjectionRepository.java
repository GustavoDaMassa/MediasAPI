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
    @Query(value = "DELETE p FROM projection p JOIN course c ON p.course_id = c.id WHERE p.course_id = :courseId AND c.user_id = :userId",nativeQuery = true)
    void deleteAllByCourse(@Param("courseId") Long courseId, @Param("userId") Long userId);

    boolean existsByCourseAndName(Course course, String name);

    @Modifying
    @Query(value = "DELETE FROM projection WHERE id = :id AND course_id = :courseId",nativeQuery = true)
    void deleteProjection(@Param("id") Long id, @Param("courseId") Long courseId);

    @Query(value = "SELECT p.* FROM projection p JOIN course c ON p.course_id = c.id JOIN users u ON c.user_id = u.id" +
            " WHERE u.id = :userId;",nativeQuery = true)
    List<Projection> findAllByUserId(@Param("userId") Long userId);

}