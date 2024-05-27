package BE.artifact.controller;

import BE.artifact.model.Onboarding;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;
    private final UserRepository userRepository;

    Logger logger = Logger.getLogger(OnboardingController.class.getName());

    @PostMapping("/start/{email}")
    public ResponseEntity<Onboarding> startOnboarding(@PathVariable String email) {
        return onboardingService.startOnboardingForUser(email);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Onboarding>> getAllOnboarding() {
        return ResponseEntity.ok(onboardingService.getAllOnboarding());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Onboarding> getOnboardingForUser(@PathVariable String email) {
        Onboarding onboarding = onboardingService.getOnboardingForUser(email);
        if (onboarding == null) {
            logger.severe("Onboarding process not found");
            throw new RuntimeException("Onboarding process not found");
        }
        return ResponseEntity.ok(onboarding);
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

    @DeleteMapping("/{id}")
    public void deleteOffboarding(@PathVariable Long id) {
        try {
            onboardingService.deleteOnboarding(id);
        } catch (Exception e) {
            logger.severe("Onboarding process not found");
            throw new RuntimeException("Onboarding process not found");
        }
    }
}
