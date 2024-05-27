package BE.artifact.dto;

import BE.artifact.model.recruiting.JobOffer;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

@Data
public class JobOfferDTO {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    List<RecommendationDTO> recommendations;
    private Date creationDate;
    private boolean active;

    public static JobOfferDTO from(JobOffer jobOffer) {
        JobOfferDTO jobOfferDTO = new JobOfferDTO();
        jobOfferDTO.setId(jobOffer.getId());
        jobOfferDTO.setTitle(jobOffer.getTitle());
        jobOfferDTO.setDescription(jobOffer.getDescription());
        jobOfferDTO.setRequirements(jobOffer.getRequirements());
        jobOfferDTO.setRecommendations(jobOffer.getRecommendations().stream().map(RecommendationDTO::from).toList());
        jobOfferDTO.setCreationDate(jobOffer.getCreationDate());
        jobOfferDTO.setActive(jobOffer.isActive());
        return jobOfferDTO;
    }

    public JobOffer toJobOffer() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(this.id);
        jobOffer.setTitle(this.title);
        jobOffer.setDescription(this.description);
        jobOffer.setRequirements(this.requirements);
        jobOffer.setCreationDate(this.creationDate);
        jobOffer.setActive(this.active);
        if (this.recommendations != null) {
            jobOffer.setRecommendations(this.recommendations.stream()
                    .map(RecommendationDTO::toRecommendation)
                    .collect(Collectors.toList()));
        } else {
            jobOffer.setRecommendations(Collections.emptyList());
        }

        return jobOffer;
    }
}
