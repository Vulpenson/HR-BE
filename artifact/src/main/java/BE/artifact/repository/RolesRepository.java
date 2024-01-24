package BE.artifact.repository;

import BE.artifact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<User, String> {
}
