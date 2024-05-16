package BE.artifact.service;

import BE.artifact.dto.PersonalDetailsDTO;
import BE.artifact.model.PersonalDetails;
import BE.artifact.repository.PersonalDetailsRepository;
import BE.artifact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalDetailsService {
    private final PersonalDetailsRepository personalDetailsRepository;
    private final UserRepository userRepository;

    public void savePersonalDetails(PersonalDetails personalDetails) {
        personalDetailsRepository.save(personalDetails);
    }

    public void deletePersonalDetailsById(Integer id) {
        personalDetailsRepository.deleteById(id);
    }

    public PersonalDetails updatePersonalDetails(Integer id, PersonalDetails personalDetails) {
        PersonalDetails personalDetailsToUpdate = personalDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personal details not found with id " + id));

        personalDetailsToUpdate.setCNP(personalDetails.getCNP());
        personalDetailsToUpdate.setPhoneNumber(personalDetails.getPhoneNumber());
        personalDetailsToUpdate.setAddress(personalDetails.getAddress());
        personalDetailsToUpdate.setCity(personalDetails.getCity());
        personalDetailsToUpdate.setCountry(personalDetails.getCountry());
        personalDetailsToUpdate.setPostalCode(personalDetails.getPostalCode());
        personalDetailsToUpdate.setBank(personalDetails.getBank());
        personalDetailsToUpdate.setBankAccount(personalDetails.getBankAccount());
        personalDetailsToUpdate.setIdentityCard(personalDetails.getIdentityCard());
        personalDetailsToUpdate.setIdentityCardSeries(personalDetails.getIdentityCardSeries());
        personalDetailsToUpdate.setIdentityCardNumber(personalDetails.getIdentityCardNumber());
        personalDetailsToUpdate.setRegisteredBy(personalDetails.getRegisteredBy());
        personalDetailsToUpdate.setRegistrationDate(personalDetails.getRegistrationDate());
        personalDetailsToUpdate.setCompanyPosition(personalDetails.getCompanyPosition());
        personalDetailsToUpdate.setContractNumber(personalDetails.getContractNumber());
        personalDetailsToUpdate.setContractStartDate(personalDetails.getContractStartDate());

        return personalDetailsRepository.save(personalDetailsToUpdate);
    }

    public PersonalDetails getPersonalDetailsByEmail(String email) {
        return personalDetailsRepository.findByUserEmail(email);
    }

    public PersonalDetails getPersonalDetailsOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        return personalDetailsRepository.findByUserEmail(currentEmail);
    }

    public void deletePersonalDetailsOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        PersonalDetails personalDetails = personalDetailsRepository.findByUserEmail(currentEmail);
        personalDetailsRepository.delete(personalDetails);
    }

    public void savePersonalDetailsOfCurrentUser(PersonalDetailsDTO personalDetailsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        PersonalDetails personalDetails = this.from(personalDetailsDTO);
        personalDetails.setUser(userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found")));
        personalDetailsRepository.save(personalDetails);
    }

    public void updatePersonalDetailsOfCurrentUser(PersonalDetailsDTO personalDetailsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        PersonalDetails personalDetailsToUpdate = personalDetailsRepository.findByUserEmail(currentEmail);
        if (personalDetailsDTO.getCNP() != null) {
            personalDetailsToUpdate.setCNP(personalDetailsDTO.getCNP());
        }
        if (personalDetailsDTO.getPhoneNumber() != null) {
            personalDetailsToUpdate.setPhoneNumber(personalDetailsDTO.getPhoneNumber());
        }
        if (personalDetailsDTO.getAddress() != null) {
            personalDetailsToUpdate.setAddress(personalDetailsDTO.getAddress());
        }
        if (personalDetailsDTO.getCity() != null) {
            personalDetailsToUpdate.setCity(personalDetailsDTO.getCity());
        }
        if (personalDetailsDTO.getCountry() != null) {
            personalDetailsToUpdate.setCountry(personalDetailsDTO.getCountry());
        }
        if (personalDetailsDTO.getPostalCode() != null) {
            personalDetailsToUpdate.setPostalCode(personalDetailsDTO.getPostalCode());
        }
        if (personalDetailsDTO.getBank() != null) {
            personalDetailsToUpdate.setBank(personalDetailsDTO.getBank());
        }
        if (personalDetailsDTO.getBankAccount() != null) {
            personalDetailsToUpdate.setBankAccount(personalDetailsDTO.getBankAccount());
        }
        if (personalDetailsDTO.getIdentityCard() != null) {
            personalDetailsToUpdate.setIdentityCard(personalDetailsDTO.getIdentityCard());
        }
        if (personalDetailsDTO.getIdentityCardSeries() != null) {
            personalDetailsToUpdate.setIdentityCardSeries(personalDetailsDTO.getIdentityCardSeries());
        }
        if (personalDetailsDTO.getIdentityCardNumber() != null) {
            personalDetailsToUpdate.setIdentityCardNumber(personalDetailsDTO.getIdentityCardNumber());
        }
        if (personalDetailsDTO.getRegisteredBy() != null) {
            personalDetailsToUpdate.setRegisteredBy(personalDetailsDTO.getRegisteredBy());
        }
        if (personalDetailsDTO.getRegistrationDate() != null) {
            personalDetailsToUpdate.setRegistrationDate(personalDetailsDTO.getRegistrationDate());
        }
        if (personalDetailsDTO.getCompanyPosition() != null) {
            personalDetailsToUpdate.setCompanyPosition(personalDetailsDTO.getCompanyPosition());
        }
        if (personalDetailsDTO.getContractNumber() != null) {
            personalDetailsToUpdate.setContractNumber(personalDetailsDTO.getContractNumber());
        }
        if (personalDetailsDTO.getContractStartDate() != null) {
            personalDetailsToUpdate.setContractStartDate(personalDetailsDTO.getContractStartDate());
        }
        if (personalDetailsDTO.getDepartment() != null) {
            personalDetailsToUpdate.setDepartment(personalDetailsDTO.getDepartment());
        }
        personalDetailsRepository.save(personalDetailsToUpdate);
    }

    public PersonalDetails from(PersonalDetailsDTO personalDetailsDTO) {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setCNP(personalDetailsDTO.getCNP());
        personalDetails.setPhoneNumber(personalDetailsDTO.getPhoneNumber());
        personalDetails.setAddress(personalDetailsDTO.getAddress());
        personalDetails.setCity(personalDetailsDTO.getCity());
        personalDetails.setCountry(personalDetailsDTO.getCountry());
        personalDetails.setPostalCode(personalDetailsDTO.getPostalCode());
        personalDetails.setBank(personalDetailsDTO.getBank());
        personalDetails.setBankAccount(personalDetailsDTO.getBankAccount());
        personalDetails.setIdentityCard(personalDetailsDTO.getIdentityCard());
        personalDetails.setIdentityCardSeries(personalDetailsDTO.getIdentityCardSeries());
        personalDetails.setIdentityCardNumber(personalDetailsDTO.getIdentityCardNumber());
        personalDetails.setRegisteredBy(personalDetailsDTO.getRegisteredBy());
        personalDetails.setRegistrationDate(personalDetailsDTO.getRegistrationDate());
        personalDetails.setCompanyPosition(personalDetailsDTO.getCompanyPosition());
        personalDetails.setContractNumber(personalDetailsDTO.getContractNumber());
        personalDetails.setContractStartDate(personalDetailsDTO.getContractStartDate());
        return personalDetails;
    }
}
