package BE.artifact.repository;

import BE.artifact.model.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Integer> {
    PersonalDetails findByUserEmail(String email);
}
