package BE.artifact.dto;

import BE.artifact.model.Onboarding;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnboardingDTO {
    private boolean badgeObtained;
    private boolean hardwareAcquired;

    public static OnboardingDTO from(Onboarding onboarding) {
        return new OnboardingDTO(onboarding.isBadgeObtained(), onboarding.isHardwareAcquired());
    }
}
