package BE.artifact.service;

import BE.artifact.dto.JobOfferDTO;
import BE.artifact.model.recruiting.JobOffer;
import BE.artifact.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public JobOffer createJobOffer(JobOfferDTO jobOffer) {
        JobOffer newJobOffer = jobOfferRepository.save(jobOffer.toJobOffer());
        newJobOffer.setCreationDate(new Date());
        return jobOfferRepository.save(newJobOffer);
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

    public Page<JobOffer> getActiveJobOffers(Pageable pageable) {
        return jobOfferRepository.findByActiveTrue(pageable);
    }
}
