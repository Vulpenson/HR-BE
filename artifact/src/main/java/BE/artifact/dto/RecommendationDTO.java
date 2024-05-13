package BE.artifact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationDTO {
    private String questionnaireResponse;

    public RecommendationDTO from(String questionnaireResponse) {
        return new RecommendationDTO(questionnaireResponse);
    }
}
