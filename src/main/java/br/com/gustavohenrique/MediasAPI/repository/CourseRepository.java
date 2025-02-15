package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
