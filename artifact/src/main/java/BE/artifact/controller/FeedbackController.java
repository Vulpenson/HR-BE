package BE.artifact.controller;

import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import BE.artifact.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitFeedback(@RequestBody @Valid Feedback feedback, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid feedback provided");
        }
        feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok("Feedback submitted successfully");
    }

    // Endpoint to retrieve all feedback (secured for admin use)
    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    // Endpoint to retrieve feedback by type (secured for admin use)
    @GetMapping("/type/{feedbackType}")
    public ResponseEntity<List<Feedback>> getFeedbackByType(@PathVariable String feedbackType) {
        List<Feedback> feedbackList = feedbackService.getFeedbackByType(feedbackType);
        return ResponseEntity.ok(feedbackList);
    }
}
