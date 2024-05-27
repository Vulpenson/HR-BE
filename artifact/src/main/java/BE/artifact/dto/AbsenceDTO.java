package BE.artifact.dto;

import BE.artifact.model.absence.Absence;
import BE.artifact.model.absence.AbsenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AbsenceDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private AbsenceType type;
    private Boolean approved;
    private String managerEmail;
    private byte[] document;

    public static AbsenceDTO from(Absence absence) {
        return new AbsenceDTO(
                absence.getStartDate(),
                absence.getEndDate(),
                absence.getType(),
                absence.isApproved(),
                absence.getManager() != null ? absence.getManager().getEmail() : null,
                absence.getDocument()
        );
    }
}
