package BE.artifact.repository;

import BE.artifact.model.User;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
