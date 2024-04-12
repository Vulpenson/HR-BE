package BE.artifact.repository;

import BE.artifact.model.PdfTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfTemplateRepository extends JpaRepository<PdfTemplate, Long> {
    PdfTemplate findByName(String name);
}
