package BE.artifact.controller;

import BE.artifact.dto.FeedbackDTO;
import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import BE.artifact.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitFeedback(@RequestBody @Valid FeedbackDTO feedback, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid feedback provided");
        }
        feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok("Feedback submitted successfully");
    }

    // Endpoint to retrieve all feedback with pagination (secured for admin use)
    @GetMapping("/all")
    public ResponseEntity<Page<FeedbackDTO>> getAllFeedback(int page, int size) {
        Page<FeedbackDTO> feedbackList = feedbackService.getAllFeedback(page, size);
        return ResponseEntity.ok(feedbackList);
    }

    // Endpoint to retrieve feedback by type with pagination (secured for admin use)
    @GetMapping("/type/{feedbackType}")
    public ResponseEntity<Page<FeedbackDTO>> getFeedbackByType(@PathVariable String feedbackType, int page, int size) {
        Page<FeedbackDTO> feedbackList = feedbackService.getFeedbackByType(feedbackType, page, size);
        return ResponseEntity.ok(feedbackList);
    }

    // Endpoint to delete feedback by id (secured for admin use)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedbackById(@PathVariable Long id) {
        feedbackService.deleteFeedbackById(id);
        return ResponseEntity.ok("Feedback deleted successfully");
    }
}
