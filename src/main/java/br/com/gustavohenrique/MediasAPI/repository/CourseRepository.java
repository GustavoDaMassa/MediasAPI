package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByUser(Users user);

    boolean existsByUserAndId(Users user, Long id);

    Optional<Course> findByUserAndId(Users user, Long id);

    boolean existsByUserdAndName(Users user, @NotBlank String name);

}
