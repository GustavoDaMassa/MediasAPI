package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByUser(Users user);

    Optional<Course> findByUserAndId(Users user, Long id);

    boolean existsByUserAndName(Users user, @NotBlank String name);

    @Modifying
    @Query(value = "DELETE FROM course WHERE id = :id",nativeQuery = true)
    void deleteCourse(@Param("id") Long id);

}
