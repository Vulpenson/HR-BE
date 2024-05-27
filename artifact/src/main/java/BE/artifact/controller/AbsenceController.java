package BE.artifact.controller;

import BE.artifact.dto.AbsenceDTO;
import BE.artifact.model.absence.Absence;
import BE.artifact.model.absence.AbsenceType;
import BE.artifact.service.AbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    public ResponseEntity<Absence> addAbsenceForCurrentUser(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                            @RequestParam("type") AbsenceType type,
                                                            @RequestParam("approved") Boolean approved,
                                                            @RequestParam("managerEmail") String managerEmail,
                                                            @RequestParam(value = "document", required = false) MultipartFile document) throws IOException {
        AbsenceDTO absenceDTO = new AbsenceDTO(startDate, endDate, type, approved, managerEmail, document != null ? document.getBytes() : null);
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
    public List<AbsenceDTO> getAbsencesByUserEmail(@PathVariable String email) {
        return absenceService.getAbsencesByUserEmail(email);
    }

    @GetMapping("/last")
    public ResponseEntity<AbsenceDTO> getLastAbsence() {
        return absenceService.getLastAbsence();
    }
}