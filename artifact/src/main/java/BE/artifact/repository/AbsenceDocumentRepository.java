package BE.artifact.repository;

import BE.artifact.model.absence.AbsenceDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceDocumentRepository extends JpaRepository<AbsenceDocument, Long> {
    List<AbsenceDocument> findByAbsenceId(Long absenceId);
}
