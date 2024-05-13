package BE.artifact.dto;

import BE.artifact.model.Offboarding;
import BE.artifact.model.Onboarding;
import BE.artifact.model.Payroll;
import BE.artifact.model.User;
import BE.artifact.model.absence.Absence;
import BE.artifact.model.recruiting.Recommendation;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private List<AbsenceDTO> absences;
    private Onboarding onboarding;
    private Offboarding offboarding;
    private List<RecommendationDTO> recommendations;
    private List<PayrollDTO> payrolls;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();

        return null;
    }
}
