package BE.artifact.repository;

import BE.artifact.model.Offboarding;
import BE.artifact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffboardingRepository extends JpaRepository<Offboarding, Long> {

}
