package BE.artifact.controller;

import BE.artifact.model.PdfTemplate;
import BE.artifact.service.PdfTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf-templates")
public class PdfTemplateController {

    @Autowired
    private PdfTemplateService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadTemplate(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(service.saveTemplate(name, file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getTemplate(@PathVariable Long id) {
        PdfTemplate template = service.getTemplate(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(template.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTemplate(@PathVariable Long id) {
        service.deleteTemplate(id);
        return ResponseEntity.ok("Template deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTemplates() {
        return ResponseEntity.ok(service.getAllTemplates());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTemplate(@PathVariable Long id, @RequestBody PdfTemplate template) {
        service.updateTemplate(id, template);
        return ResponseEntity.ok("Template updated successfully");
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteTemplateByName(@PathVariable String name) {
        service.deleteTemplateByName(name);
        return ResponseEntity.ok("Template deleted successfully");
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<String> updateTemplateByName(@PathVariable String name, @RequestBody PdfTemplate template) {
        service.updateTemplateByName(name, template);
        return ResponseEntity.ok("Template updated successfully");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getTemplateByName(@PathVariable String name) {
        return ResponseEntity.ok(service.getTemplateByName(name));
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> downloadTemplateByName(@PathVariable String name) {
        PdfTemplate template = service.getTemplateByName(name);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(template.getContent());
    }
}
