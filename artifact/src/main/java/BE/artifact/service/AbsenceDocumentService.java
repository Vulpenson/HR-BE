package BE.artifact.service;

import BE.artifact.model.absence.Absence;
import BE.artifact.model.absence.AbsenceDocument;
import BE.artifact.repository.AbsenceDocumentRepository;
import BE.artifact.repository.AbsenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceDocumentService {

    private final AbsenceDocumentRepository documentRepository;
    private final AbsenceRepository absenceRepository;

    public AbsenceDocument storeDocument(MultipartFile file, Long absenceId) throws IOException {
        String fileName = file.getOriginalFilename();
        String mimeType = file.getContentType();
        byte[] data = file.getBytes();

        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(() -> new RuntimeException("Absence not found with id " + absenceId));

        AbsenceDocument document = new AbsenceDocument();
        document.setName(fileName);
        document.setMimeType(mimeType);
        document.setData(data);
        document.setAbsence(absence);

        return documentRepository.save(document);
    }

    public AbsenceDocument getDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id " + id));
    }

    public List<AbsenceDocument> getDocumentsByAbsenceId(Long absenceId) {
        return documentRepository.findByAbsenceId(absenceId);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public void deleteDocumentsByAbsenceId(Long absenceId) {
        documentRepository.deleteByAbsenceId(absenceId);
    }

    public List<AbsenceDocument> getAllDocuments() {
        return documentRepository.findAll();
    }

    public AbsenceDocument updateDocument(Long id, AbsenceDocument document) {
        AbsenceDocument existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id " + id));

        existingDocument.setName(document.getName());
        existingDocument.setMimeType(document.getMimeType());
        existingDocument.setData(document.getData());

        return documentRepository.save(existingDocument);
    }
}
