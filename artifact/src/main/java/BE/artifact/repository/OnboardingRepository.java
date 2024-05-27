package BE.artifact.repository;

import BE.artifact.model.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
    Onboarding findByUserEmail(String email);
}
