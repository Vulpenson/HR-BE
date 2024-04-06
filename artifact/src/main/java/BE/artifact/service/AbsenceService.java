package BE.artifact.service;

import BE.artifact.dto.AbsenceDTO;
import BE.artifact.model.absence.Absence;
import BE.artifact.repository.AbsenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepository absenceRepository;

    public ResponseEntity<Absence> saveAbsence(AbsenceDTO absenceDTO) {
        Absence absence = mapDtoToEntity(absenceDTO);
        Absence savedAbsence = absenceRepository.save(absence);
        return ResponseEntity.ok(savedAbsence);
    }

    public List<Absence> getAllAbsences() {
        return absenceRepository.findAll();
    }

    private Absence mapDtoToEntity(AbsenceDTO absenceDTO) {
        Absence absence = new Absence();
        absence.setStartDate(absenceDTO.getStartDate());
        absence.setEndDate(absenceDTO.getEndDate());
        absence.setType(absenceDTO.getType());
        // Set the user reference here if necessary
        return absence;
    }
}
