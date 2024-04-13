package BE.artifact.service;

import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import BE.artifact.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Transactional
    public void submitFeedback(Feedback feedback) {
        if(feedback.getFeedbackType() == null) {
            feedback.setFeedbackType(FeedbackType.OTHER); // Set the feedback type to OTHER if it is not provided
        }
        feedbackRepository.save(feedback); // Save the anonymous feedback
    }

    // Method to list all feedback (if needed for admin)
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    // Method to list all feedback of a specific type (if needed for admin)
    public List<Feedback> getFeedbackByType(FeedbackType feedbackType) {
        return feedbackRepository.findByFeedbackType(feedbackType);
    }

    // Method to list all feedback of a specific type (if needed for admin)
    public List<Feedback> getFeedbackByType(String feedbackType) {
        FeedbackType type = null;
        try {
            type = FeedbackType.valueOf(feedbackType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return feedbackRepository.findByFeedbackType(type);
    }
}
