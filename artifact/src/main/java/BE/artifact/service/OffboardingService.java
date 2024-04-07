package BE.artifact.service;

import BE.artifact.model.Offboarding;
import BE.artifact.model.User;
import BE.artifact.repository.OffboardingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OffboardingService {

    private final OffboardingRepository offboardingRepository;

    @Transactional
    public Offboarding startOffboardingForUser(User user) {
        Offboarding offboarding = new Offboarding();
        offboarding.setUser(user);
        offboarding.setAccountDeactivated(false);
        offboarding.setHardwareReturned(false);
        return offboardingRepository.save(offboarding);
    }

    public Iterable<Offboarding> getAllOffboarding() {
        return offboardingRepository.findAll();
    }

    public Offboarding getOffboardingForUser(User user) {
        return offboardingRepository.findById(user.getId().longValue())
                .orElseThrow(() -> new RuntimeException("Offboarding process not found"));
    }

    @Transactional
    public void updateAccountDeactivationStatus(Long offboardingId, boolean status) {
        Offboarding offboarding = offboardingRepository.findById(offboardingId)
                .orElseThrow(() -> new RuntimeException("Offboarding process not found"));
        offboarding.setAccountDeactivated(status);
        offboardingRepository.save(offboarding);
    }

    @Transactional
    public void updateHardwareReturnStatus(Long offboardingId, boolean status) {
        Offboarding offboarding = offboardingRepository.findById(offboardingId)
                .orElseThrow(() -> new RuntimeException("Offboarding process not found"));
        offboarding.setHardwareReturned(status);
        offboardingRepository.save(offboarding);
    }

    @Transactional
    public void deleteOffboarding(Long id) {
        offboardingRepository.deleteById(id);
    }
}
