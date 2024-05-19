package BE.artifact.dto;

import BE.artifact.model.recruiting.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationDTO {
    private String questionnaireResponse;

    public static RecommendationDTO from(Recommendation recommendation) {
        return new RecommendationDTO(recommendation.getQuestionnaireResponse());
    }

    public Recommendation toRecommendation() {
        Recommendation recommendation = new Recommendation();
        recommendation.setQuestionnaireResponse(this.questionnaireResponse);
        return recommendation;
    }
}
