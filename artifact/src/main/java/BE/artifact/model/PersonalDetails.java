package BE.artifact.model;

import BE.artifact.security.AttributeEncryptor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PersonalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @Convert(converter = AttributeEncryptor.class)
    private String CNP;

    @Convert(converter = AttributeEncryptor.class)
    private String phoneNumber;

    @Convert(converter = AttributeEncryptor.class)
    private String address;

    @Convert(converter = AttributeEncryptor.class)
    private String city;

    @Convert(converter = AttributeEncryptor.class)
    private String country;

    @Convert(converter = AttributeEncryptor.class)
    private String postalCode;

    @Convert(converter = AttributeEncryptor.class)
    private String bank;

    @Convert(converter = AttributeEncryptor.class)
    private String bankAccount;

    @Convert(converter = AttributeEncryptor.class)
    private String identityCard;

    @Convert(converter = AttributeEncryptor.class)
    private String identityCardSeries;

    @Convert(converter = AttributeEncryptor.class)
    private String identityCardNumber;

    @Convert(converter = AttributeEncryptor.class)
    private String registeredBy;

    @Convert(converter = AttributeEncryptor.class)
    private String registrationDate;

    @Convert(converter = AttributeEncryptor.class)
    private String companyPosition;

    @Convert(converter = AttributeEncryptor.class)
    private String contractNumber;

    @Convert(converter = AttributeEncryptor.class)
    private String contractStartDate;
}
