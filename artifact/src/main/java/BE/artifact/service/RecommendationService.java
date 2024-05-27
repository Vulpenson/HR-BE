package BE.artifact.service;

import BE.artifact.controller.RecommendationController;
import BE.artifact.dto.RecommendationDTO;
import BE.artifact.model.User;
import BE.artifact.model.recruiting.JobOffer;
import BE.artifact.model.recruiting.Recommendation;
import BE.artifact.repository.JobOfferRepository;
import BE.artifact.repository.RecommendationRepository;
import BE.artifact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.modelmapper.Converters.Collection.map;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;

    Logger logger = Logger.getLogger(RecommendationService.class.getName());

    public Recommendation saveRecommendation(Long jobOfferId, String questionnaireResponse, MultipartFile cvFile) throws IOException {
        try {
            Recommendation recommendation = new Recommendation();

            // Fetch the recommending user as the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();
            User user = userRepository.findByEmail(currentEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            recommendation.setUser(user);

            // Fetch the job offer
            JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElseThrow(() -> new RuntimeException("Job Offer not found"));
            recommendation.setJobOffer(jobOffer);

            // Set the questionnaire response
            recommendation.setQuestionnaireResponse(questionnaireResponse);

            // Save the CV file
            byte[] cvBytes = cvFile.getBytes();
            recommendation.setCvContent(cvBytes);
            return recommendationRepository.save(recommendation);
        } catch (IOException e) {
            logger.severe("Failed to save CV file");
            throw new RuntimeException("Failed to save CV file", e);
        }
    }

    public Iterable<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    public Recommendation getRecommendationById(Long id) {
        return recommendationRepository.findById(id).orElse(null);
    }

    public void deleteRecommendation(Long id) {
        recommendationRepository.deleteById(id);
    }

    public Recommendation updateRecommendation(Long id, Recommendation recommendation) {
        Recommendation existingRecommendation = recommendationRepository.findById(id).orElse(null);
        if (existingRecommendation != null) {
            existingRecommendation.setQuestionnaireResponse(recommendation.getQuestionnaireResponse());
            existingRecommendation.setCvContent(recommendation.getCvContent());
            return recommendationRepository.save(existingRecommendation);
        }
        return null;
    }

    public Iterable<Recommendation> getCurrentUserRecommendations() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();
            return recommendationRepository.findByUserEmail(currentEmail);
        } catch (Exception e) {
            logger.severe("Failed to get recommendations for the current user");
            throw new RuntimeException(e);
        }
    }

    public Recommendation saveRecommendationOfCurrentUser(Long jobOfferId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();
            User user = userRepository.findByEmail(currentEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            byte[] cvContent = user.getCvContent();

            JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                    .orElseThrow(() -> new RuntimeException("Job Offer not found"));

            Recommendation recommendation = new Recommendation();
            recommendation.setUser(user);
            recommendation.setJobOffer(jobOffer);
            recommendation.setQuestionnaireResponse("Self-recommendation");
            recommendation.setCvContent(cvContent);

            return recommendationRepository.save(recommendation);
        } catch (Exception e) {
            logger.severe("Failed to save recommendation for the current user");
            throw new RuntimeException(e);
        }
    }

    public Iterable<RecommendationDTO> getRecommendationsForJobOffer(Long jobOfferId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Job Offer not found"));
        Iterable<Recommendation> recommendations = recommendationRepository.findByJobOffer(jobOffer);
        return StreamSupport.stream(recommendations.spliterator(), false)
                .map(RecommendationDTO::from)
                .collect(Collectors.toList());
    }
}