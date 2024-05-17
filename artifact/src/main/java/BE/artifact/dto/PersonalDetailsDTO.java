package BE.artifact.dto;

import BE.artifact.model.PersonalDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonalDetailsDTO {
    private String CNP;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String bank;
    private String bankAccount;
    private String identityCard;
    private String identityCardSeries;
    private String identityCardNumber;
    private String registeredBy;
    private String registrationDate;
    private String companyPosition;
    private String contractNumber;
    private String contractStartDate;
    private String department;

    public static PersonalDetailsDTO from(PersonalDetails personalDetails) {
        return new PersonalDetailsDTO(
                personalDetails.getCNP(),
                personalDetails.getPhoneNumber(),
                personalDetails.getAddress(),
                personalDetails.getCity(),
                personalDetails.getCountry(),
                personalDetails.getPostalCode(),
                personalDetails.getBank(),
                personalDetails.getBankAccount(),
                personalDetails.getIdentityCard(),
                personalDetails.getIdentityCardSeries(),
                personalDetails.getIdentityCardNumber(),
                personalDetails.getRegisteredBy(),
                personalDetails.getRegistrationDate(),
                personalDetails.getCompanyPosition(),
                personalDetails.getContractNumber(),
                personalDetails.getContractStartDate(),
                personalDetails.getDepartment());
    }
}
