package BE.artifact.service;

import BE.artifact.model.Onboarding;
import BE.artifact.model.User;
import BE.artifact.repository.OnboardingRepository;
import BE.artifact.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final OnboardingRepository onboardingRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<Onboarding> startOnboardingForUser(String email) {
        Onboarding onboarding = new Onboarding();
        onboarding.setUser(userRepository.findByEmail(email).orElseThrow());
        onboarding.setBadgeObtained(false);
        onboarding.setHardwareAcquired(false);
        return ResponseEntity.ok(onboardingRepository.save(onboarding));
    }

    public Iterable<Onboarding> getAllOnboarding() {
        return onboardingRepository.findAll();
    }

    @Transactional
    public Onboarding getOnboardingForUser(String email) {
        Onboarding onboarding = onboardingRepository.findByUserEmail(email);
        if (onboarding == null) {
            onboarding = startOnboardingForUser(email).getBody();
        }
        return onboarding;
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

    @Transactional
    public void deleteOnboarding(Long id) {
        onboardingRepository.deleteById(id);
    }
}