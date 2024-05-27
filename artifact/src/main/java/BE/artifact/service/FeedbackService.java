package BE.artifact.service;

import BE.artifact.dto.FeedbackDTO;
import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import BE.artifact.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Transactional
    public void submitFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackDTO.toFeedback();
        if(feedback.getFeedbackType() == null) {
            feedback.setFeedbackType(FeedbackType.OTHER); // Set the feedback type to OTHER if it is not provided
        }
        feedbackRepository.save(feedback); // Save the anonymous feedback
    }

    // Method to list all feedback with pagination
    public Page<FeedbackDTO> getAllFeedback(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateSubmitted").descending());
        return feedbackRepository.findAll(pageable).map(FeedbackDTO::from);
    }

    // Method to list all feedback of a specific type with pagination
    public Page<FeedbackDTO> getFeedbackByType(String feedbackType, int page, int size) {
        FeedbackType type;
        try {
            type = FeedbackType.valueOf(feedbackType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateSubmitted").descending());
        return feedbackRepository.findByFeedbackType(type, pageable).map(FeedbackDTO::from);
    }

    // Method to delete feedback by id (if needed for admin)
    public void deleteFeedbackById(Long id) {
        feedbackRepository.deleteById(id);
    }
}
