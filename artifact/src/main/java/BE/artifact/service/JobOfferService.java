package BE.artifact.service;

import BE.artifact.model.recruiting.JobOffer;
import BE.artifact.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public JobOffer createJobOffer(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }

    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public JobOffer getJobOfferById(Long id) {
        return jobOfferRepository.findById(id).orElse(null);
    }

    public void deleteJobOffer(Long id) {
        jobOfferRepository.deleteById(id);
    }

    public JobOffer updateJobOffer(Long id, JobOffer jobOffer) {
        JobOffer existingJobOffer = jobOfferRepository.findById(id).orElse(null);
        if (existingJobOffer != null) {
            existingJobOffer.setTitle(jobOffer.getTitle());
            existingJobOffer.setDescription(jobOffer.getDescription());
            existingJobOffer.setRequirements(jobOffer.getRequirements());
            return jobOfferRepository.save(existingJobOffer);
        }
        return null;
    }

    public List<JobOffer> getActiveJobOffers() {
        return jobOfferRepository.findByActiveTrue();
    }
}
