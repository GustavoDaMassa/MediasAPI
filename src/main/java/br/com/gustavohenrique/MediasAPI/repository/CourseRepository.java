package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Course;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByUserId(Long userId);

    boolean existsByUserIdAndId(Long userId, Long id);

    Optional<Course> findByUserIdAndId(Long userId, Long id);

    boolean existsByUserIdAndName(Long userId, @NotBlank String name);

    void deleteAllByUserId(Long userId);

}
