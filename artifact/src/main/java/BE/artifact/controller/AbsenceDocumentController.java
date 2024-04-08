package BE.artifact.controller;

import BE.artifact.model.absence.AbsenceDocument;
import BE.artifact.service.AbsenceDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/absence-documents")
@RequiredArgsConstructor
public class AbsenceDocumentController {

    private final AbsenceDocumentService documentService;


    @PostMapping("/upload/{absenceId}")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file, @PathVariable Long absenceId) throws IOException {
        AbsenceDocument document = documentService.storeDocument(file, absenceId);
        return ResponseEntity.ok("Document stored with ID: " + document.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long id) {
        AbsenceDocument document = documentService.getDocument(id);
        return ResponseEntity.ok()
                .header("Content-Type", document.getMimeType())
                .header("Content-Disposition", "attachment; filename=\"" + document.getName() + "\"")
                .body(document.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok("Document deleted");
    }

    @DeleteMapping("/absence/{absenceId}")
    public ResponseEntity<?> deleteDocumentsForAbsence(@PathVariable Long absenceId) {
        documentService.deleteDocumentsByAbsenceId(absenceId);
        return ResponseEntity.ok("Documents deleted");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/absence/{absenceId}")
    public ResponseEntity<?> getDocumentsForAbsence(@PathVariable Long absenceId) {
        return ResponseEntity.ok(documentService.getDocumentsByAbsenceId(absenceId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable Long id, @RequestBody AbsenceDocument document) {
        return ResponseEntity.ok(documentService.updateDocument(id, document));
    }
}
