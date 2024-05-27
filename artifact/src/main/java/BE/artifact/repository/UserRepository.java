package BE.artifact.repository;

import BE.artifact.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    List<User> findAll();
    List<User> findByManagerEmail(String manager_email);
}
