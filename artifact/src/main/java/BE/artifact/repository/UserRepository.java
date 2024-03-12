package BE.artifact.repository;

import BE.artifact.model.User;
import jakarta.transaction.Transactional;
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

    @Modifying
    @Transactional
    @Query("DELETE from User u WHERE u.username = :username")
    void deleteByUsername(String username);

}
