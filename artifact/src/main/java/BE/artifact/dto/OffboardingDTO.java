package BE.artifact.dto;

import BE.artifact.model.Offboarding;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OffboardingDTO {
    private boolean accountDeactivated;
    private boolean hardwareReturned;

    public OffboardingDTO from(Offboarding offboarding) {
        return new OffboardingDTO(offboarding.isAccountDeactivated(), offboarding.isHardwareReturned());
    }
}
