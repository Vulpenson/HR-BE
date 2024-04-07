package BE.artifact.controller;

import BE.artifact.model.Offboarding;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.OffboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/offboarding")
@RequiredArgsConstructor
public class OffboardingController {
    private final OffboardingService offboardingService;
    private final UserRepository userRepository;

    Logger logger = Logger.getLogger(OffboardingController.class.getName());

    @PostMapping("/start/{email}")
    public ResponseEntity<Offboarding> startOffboarding(@PathVariable String email) {
        return ResponseEntity.ok(offboardingService.startOffboardingForUser(userRepository.findByEmail(email).orElseThrow()));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Offboarding>> getAllOffboarding() {
        return ResponseEntity.ok(offboardingService.getAllOffboarding());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Offboarding> getOffboardingForUser(@PathVariable String email) {
        return ResponseEntity.ok(offboardingService.getOffboardingForUser(userRepository.findByEmail(email).orElseThrow()));
    }

    @PatchMapping("/account-deactivation/{offboardingId}")
    public ResponseEntity<String> updateAccountDeactivationStatus(@PathVariable Long offboardingId, @RequestBody boolean status) {
        offboardingService.updateAccountDeactivationStatus(offboardingId, status);
        return ResponseEntity.ok("Account deactivation status updated successfully.");
    }

    @PatchMapping("/hardware-return/{offboardingId}")
    public ResponseEntity<String> updateHardwareReturnStatus(@PathVariable Long offboardingId, @RequestBody boolean status) {
        offboardingService.updateHardwareReturnStatus(offboardingId, status);
        return ResponseEntity.ok("Hardware return status updated successfully.");
    }

    @DeleteMapping("/{id}")
    public void deleteOffboarding(@PathVariable Long id) {
        try {
            offboardingService.deleteOffboarding(id);
        } catch (Exception e) {
            logger.severe("Offboarding process not found");
            throw new RuntimeException("Offboarding process not found");
        }
    }
}
