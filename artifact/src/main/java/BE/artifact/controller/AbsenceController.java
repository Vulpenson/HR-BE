package BE.artifact.controller;

import BE.artifact.dto.AbsenceDTO;
import BE.artifact.model.absence.Absence;
import BE.artifact.service.AbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    @Autowired
    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @PostMapping("/add-test")
    public ResponseEntity<Absence> addAbsence(@RequestBody AbsenceDTO absenceDTO) {
        return absenceService.saveAbsence(absenceDTO);
    }

    @PostMapping("/add-current-user")
    public ResponseEntity<Absence> addAbsenceForCurrentUser(@RequestBody AbsenceDTO absenceDTO) {
        return absenceService.addAbsenceForCurrentUser(absenceDTO);
    }

    @GetMapping("/all")
    public List<Absence> getAllAbsences() {
        return absenceService.getAllAbsences();
    }

    //Absence for current user
    @GetMapping("/current-user")
    public List<Absence> getAbsencesForCurrentUser() {
        return absenceService.getAbsencesForCurrentUser();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAbsence(@PathVariable Long id) {
        return absenceService.deleteAbsence(id);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<Absence> approveAbsence(@PathVariable Long id) {
        return absenceService.approveAbsence(id);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Absence> updateAbsence(@PathVariable Long id, @RequestBody AbsenceDTO absenceDTO) {
        return absenceService.updateAbsence(id, absenceDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Absence> getAbsenceById(@PathVariable Long id) {
        return absenceService.getAbsenceById(id);
    }

    @GetMapping("/user/{email}")
    public List<Absence> getAbsencesByUserEmail(@PathVariable String email) {
        return absenceService.getAbsencesByUserEmail(email);
    }

    @GetMapping("/last")
    public ResponseEntity<AbsenceDTO> getLastAbsence() {
        return absenceService.getLastAbsence();
    }
}