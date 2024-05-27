package BE.artifact.repository;

import BE.artifact.dto.JobOfferDTO;
import BE.artifact.model.recruiting.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    Page<JobOffer> findByActiveTrue(Pageable pageable);
}
