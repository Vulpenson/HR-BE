package BE.artifact.dto;

import BE.artifact.model.absence.Absence;
import BE.artifact.model.absence.AbsenceType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AbsenceDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private AbsenceType type; // e.g., VACATION, SICK_LEAVE
    private Boolean approved;

    public static AbsenceDTO from(Absence absence) {
        return new AbsenceDTO(absence.getStartDate(), absence.getEndDate(), absence.getType(), absence.isApproved());
    }
}
