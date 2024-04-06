package BE.artifact.service;

import BE.artifact.dto.AbsenceDTO;
import BE.artifact.model.User;
import BE.artifact.model.absence.Absence;
import BE.artifact.repository.AbsenceRepository;
import BE.artifact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Absence> saveAbsence(AbsenceDTO absenceDTO) {
        Absence absence = mapDtoToEntity(absenceDTO, null);
        Absence savedAbsence = absenceRepository.save(absence);
        return ResponseEntity.ok(savedAbsence);
    }

    public ResponseEntity<Absence> addAbsenceForCurrentUser(AbsenceDTO absenceDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        System.out.println("Current email: " + currentEmail);

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Absence absence = mapDtoToEntity(absenceDTO, user);
        Absence savedAbsence = absenceRepository.save(absence);
        return ResponseEntity.ok(savedAbsence);
    }

    public List<Absence> getAllAbsences() {
        return absenceRepository.findAll();
    }

    private Absence mapDtoToEntity(AbsenceDTO absenceDTO, User user) {
        Absence absence = new Absence();
        absence.setUser(user);
        absence.setStartDate(absenceDTO.getStartDate());
        absence.setEndDate(absenceDTO.getEndDate());
        absence.setType(absenceDTO.getType());
        absence.setApproved(absenceDTO.getApproved());
        // Set the user reference here if necessary
        return absence;
    }

    // Method to get all absences for the current user
    public List<Absence> getAbsencesForCurrentUser() {
        // Implement the logic to get all absences for the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        return absenceRepository.findByUserEmail(currentEmail);
    }

    // Method to delete an absence
    public ResponseEntity<?> deleteAbsence(Long id) {
        absenceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Method to approve an absence
    public ResponseEntity<Absence> approveAbsence(Long id) {
        Absence absence = absenceRepository.findById(id).orElse(null);
        if (absence == null) {
            return ResponseEntity.notFound().build();
        }
        absence.setApproved(true);
        Absence updatedAbsence = absenceRepository.save(absence);
        return ResponseEntity.ok(updatedAbsence);
    }

    // Method to update an absence
    public ResponseEntity<Absence> updateAbsence(Long id, AbsenceDTO absenceDTO) {
        Absence absence = absenceRepository.findById(id).orElse(null);
        if (absence == null) {
            return ResponseEntity.notFound().build();
        }
        absence.setStartDate(absenceDTO.getStartDate());
        absence.setEndDate(absenceDTO.getEndDate());
        absence.setType(absenceDTO.getType());
        absence.setApproved(absenceDTO.getApproved());
        Absence updatedAbsence = absenceRepository.save(absence);
        return ResponseEntity.ok(updatedAbsence);
    }
}
