package BE.artifact.controller;

import BE.artifact.model.Onboarding;
import BE.artifact.model.User;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.OnboardingService;
import BE.artifact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;
    private final UserRepository userRepository;

    @PostMapping("/start/{email}")
    public ResponseEntity<Onboarding> startOnboarding(@PathVariable String email) {
        return onboardingService.startOnboardingForUser(userRepository.findByEmail(email).orElseThrow());
    }

    @PatchMapping("/badge/{onboardingId}")
    public ResponseEntity<String> updateBadgeStatus(@PathVariable Long onboardingId, @RequestBody boolean status) {
        onboardingService.updateBadgeStatus(onboardingId, status);
        return ResponseEntity.ok("Badge status updated successfully.");
    }

    @PatchMapping("/hardware/{onboardingId}")
    public ResponseEntity<String> updateHardwareStatus(@PathVariable Long onboardingId, @RequestBody boolean status) {
        onboardingService.updateHardwareStatus(onboardingId, status);
        return ResponseEntity.ok("Hardware status updated successfully.");
    }
}
