package BE.artifact.controller;

import BE.artifact.model.recruiting.Recommendation;
import BE.artifact.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;


    @PostMapping("/{jobOfferId}")
    public ResponseEntity<?> recommend(@PathVariable Long jobOfferId,
                                       @RequestParam("questionnaireResponse") String questionnaireResponse,
                                       @RequestParam("cvFile") MultipartFile cvFile) {
        try {
            Recommendation savedRecommendation = recommendationService.saveRecommendation(jobOfferId, questionnaireResponse, cvFile);
            return ResponseEntity.ok().body(savedRecommendation);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to save CV file");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecommendations() {
        return ResponseEntity.ok().body(recommendationService.getAllRecommendations());
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUserRecommendations() {
        return ResponseEntity.ok().body(recommendationService.getCurrentUserRecommendations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecommendationById(@PathVariable Long id) {
        return ResponseEntity.ok().body(recommendationService.getRecommendationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecommendation(@PathVariable Long id) {
        try {
            recommendationService.deleteRecommendation(id);
            return ResponseEntity.ok().body("Recommendation deleted");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete recommendation");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRecommendation(@PathVariable Long id, @RequestBody Recommendation recommendation) {
        try {
            Recommendation updatedRecommendation = recommendationService.updateRecommendation(id, recommendation);
            return ResponseEntity.ok().body(updatedRecommendation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update recommendation");
        }
    }

    @PostMapping("/apply-job")
    public ResponseEntity<?> saveRecommendationOfCurrentUser() {
        try {
            Recommendation recommendation = recommendationService.saveRecommendationOfCurrentUser();
            return ResponseEntity.ok().body(recommendation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to save recommendation");
        }
    }
}
