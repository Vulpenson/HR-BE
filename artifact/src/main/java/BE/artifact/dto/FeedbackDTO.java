package BE.artifact.dto;

import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import lombok.Data;

import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Data
public class FeedbackDTO {
    private FeedbackType feedbackType;
    private String feedbackText;
    private Date dateSubmitted;

    public static FeedbackDTO from(Feedback feedback) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFeedbackType(feedback.getFeedbackType());
        feedbackDTO.setFeedbackText(feedback.getFeedback());
        feedbackDTO.setDateSubmitted(feedback.getDateSubmitted());
        return feedbackDTO;
    }

    public Feedback toFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackType(feedbackType);
        feedback.setFeedback(this.feedbackText);
        feedback.setDateSubmitted(new Date(currentTimeMillis()));
        return feedback;
    }
}
