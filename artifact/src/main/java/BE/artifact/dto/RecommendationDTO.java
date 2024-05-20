package BE.artifact.dto;

import BE.artifact.model.recruiting.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {
    private String questionnaireResponse;
    private byte[] cvContent;
    private String name;

    public static RecommendationDTO from(Recommendation recommendation) {
        RecommendationDTO recommendationDTO = new RecommendationDTO();
        recommendationDTO.setQuestionnaireResponse(recommendation.getQuestionnaireResponse());
        recommendationDTO.setCvContent(recommendation.getCvContent());
        recommendationDTO.setName(recommendation.getUser().getFirstName() + " " + recommendation.getUser().getLastName());
        return recommendationDTO;
    }

    public Recommendation toRecommendation() {
        Recommendation recommendation = new Recommendation();
        recommendation.setQuestionnaireResponse(this.questionnaireResponse);
        recommendation.setCvContent(this.cvContent);
        return recommendation;
    }
}
