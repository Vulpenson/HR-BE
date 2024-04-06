package BE.artifact.service;

import BE.artifact.model.Onboarding;
import BE.artifact.model.User;
import BE.artifact.repository.OnboardingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final OnboardingRepository onboardingRepository;

    @Transactional
    public ResponseEntity<Onboarding> startOnboardingForUser(User user) {
        Onboarding onboarding = new Onboarding();
        onboarding.setUser(user);
        onboarding.setBadgeObtained(false);
        onboarding.setHardwareAcquired(false);
        return ResponseEntity.ok(onboardingRepository.save(onboarding));
    }

    @Transactional
    public void updateBadgeStatus(Long onboardingId, boolean status) {
        Onboarding onboarding = onboardingRepository.findById(onboardingId)
                .orElseThrow(() -> new RuntimeException("Onboarding process not found"));
        onboarding.setBadgeObtained(status);
        onboardingRepository.save(onboarding);
    }

    @Transactional
    public void updateHardwareStatus(Long onboardingId, boolean status) {
        Onboarding onboarding = onboardingRepository.findById(onboardingId)
                .orElseThrow(() -> new RuntimeException("Onboarding process not found"));
        onboarding.setHardwareAcquired(status);
        onboardingRepository.save(onboarding);
    }
}