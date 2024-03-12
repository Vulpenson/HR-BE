package BE.artifact.repository;

import BE.artifact.model.Roles;
import BE.artifact.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface RolesRepository extends JpaRepository<Roles, String> {
    Optional<Roles> findByName(UserRole name);
}
