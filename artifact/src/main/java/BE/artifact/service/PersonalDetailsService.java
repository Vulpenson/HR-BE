package BE.artifact.service;

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

    public void savePersonalDetailsOfCurrentUser(PersonalDetails personalDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        personalDetails.setUser(userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found")));
        personalDetailsRepository.save(personalDetails);
    }

    public void updatePersonalDetailsOfCurrentUser(PersonalDetails personalDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        PersonalDetails personalDetailsToUpdate = personalDetailsRepository.findByUserEmail(currentEmail);
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
        personalDetailsRepository.save(personalDetailsToUpdate);
    }
}
