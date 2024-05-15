package BE.artifact.dto;

import BE.artifact.model.User;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private List<AbsenceDTO> absences;
    private OnboardingDTO onboarding;
    private OffboardingDTO offboarding;
    private List<RecommendationDTO> recommendations;
    private List<PayrollDTO> payrolls;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().name());
        if(user.getAbsences() == null) userDTO.setAbsences(Collections.emptyList());
        userDTO.setAbsences(user.getAbsences().stream().map(AbsenceDTO::from).toList());
        if(user.getOnboarding() == null) {
            userDTO.setOnboarding(null);
        } else {
            userDTO.setOnboarding(OnboardingDTO.from(user.getOnboarding()));
        }
        if (user.getOffboarding() == null) {
            userDTO.setOffboarding(null);
        } else {
            userDTO.setOffboarding(OffboardingDTO.from(user.getOffboarding()));
        }
        if (user.getRecommendations() == null) {
            userDTO.setRecommendations(Collections.emptyList());
        } else {
            userDTO.setRecommendations(user.getRecommendations().stream().map(RecommendationDTO::from).toList());
        }
        if (user.getPayrolls() == null) {
            userDTO.setPayrolls(Collections.emptyList());
        } else {
            userDTO.setPayrolls(user.getPayrolls().stream().map(PayrollDTO::from).toList());
        }
        return userDTO;
    }
}
