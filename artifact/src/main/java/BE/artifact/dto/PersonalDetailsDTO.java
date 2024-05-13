package BE.artifact.dto;

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

    public PersonalDetailsDTO from(String CNP, String phoneNumber, String address, String city, String country,
                                   String postalCode, String bank, String bankAccount, String identityCard,
                                   String identityCardSeries, String identityCardNumber, String registeredBy,
                                   String registrationDate, String companyPosition, String contractNumber,
                                   String contractStartDate) {
        return new PersonalDetailsDTO(CNP, phoneNumber, address, city, country, postalCode, bank, bankAccount,
                identityCard, identityCardSeries, identityCardNumber, registeredBy, registrationDate, companyPosition,
                contractNumber, contractStartDate);
    }
}
