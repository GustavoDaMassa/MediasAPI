package br.com.gustavohenrique.MediasAPI.repository;

import br.com.gustavohenrique.MediasAPI.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}
