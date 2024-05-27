package BE.artifact.repository;

import BE.artifact.model.User;
import BE.artifact.model.recruiting.JobOffer;
import BE.artifact.model.recruiting.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Iterable<Recommendation> findByUserEmail(String email);

    Iterable<Recommendation> findByJobOffer(JobOffer jobOffer);
}
