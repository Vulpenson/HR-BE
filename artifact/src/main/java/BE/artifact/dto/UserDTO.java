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
    private PersonalDetailsDTO personalDetails;
    private Double grossPay;
    private byte[] cvContent;
    private String managerEmail;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().name());
        userDTO.setAbsences(user.getAbsences() != null ? user.getAbsences().stream().map(AbsenceDTO::from).toList() : Collections.emptyList());
        userDTO.setOnboarding(user.getOnboarding() != null ? OnboardingDTO.from(user.getOnboarding()) : null);
        userDTO.setOffboarding(user.getOffboarding() != null ? OffboardingDTO.from(user.getOffboarding()) : null);
        userDTO.setRecommendations(user.getRecommendations() != null ? user.getRecommendations().stream().map(RecommendationDTO::from).toList() : Collections.emptyList());
        userDTO.setPayrolls(user.getPayrolls() != null ? user.getPayrolls().stream().map(PayrollDTO::from).toList() : Collections.emptyList());
        userDTO.setPersonalDetails(user.getPersonalDetails() != null ? PersonalDetailsDTO.from(user.getPersonalDetails()) : null);
        userDTO.setGrossPay(user.getGrossPay());
        userDTO.setCvContent(user.getCvContent());
        userDTO.setManagerEmail(user.getManager() != null ? user.getManager().getEmail() : null);
        return userDTO;
    }
}
