package BE.artifact.service;

import BE.artifact.model.PdfTemplate;
import BE.artifact.repository.PdfTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PdfTemplateService {

    @Autowired
    private PdfTemplateRepository repository;

    public PdfTemplate saveTemplate(String name, MultipartFile file) throws IOException {
        PdfTemplate template = new PdfTemplate();
        template.setName(name);
        template.setContent(file.getBytes());
        return repository.save(template);
    }

    public PdfTemplate getTemplate(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public PdfTemplate getTemplateByName(String name) {
        return repository.findByName(name);
    }

    public void deleteTemplate(Long id) {
        repository.deleteById(id);
    }

    public void deleteTemplateByName(String name) {
        PdfTemplate template = repository.findByName(name);
        if (template != null) {
            repository.delete(template);
        }
    }

    public void updateTemplate(Long id, PdfTemplate template) {
        PdfTemplate existingTemplate = repository.findById(id).orElseThrow(() -> new RuntimeException("Template not found"));
        existingTemplate.setName(template.getName());
        existingTemplate.setContent(template.getContent());
        repository.save(existingTemplate);
    }

    public void updateTemplateByName(String name, PdfTemplate template) {
        PdfTemplate existingTemplate = repository.findByName(name);
        if (existingTemplate != null) {
            existingTemplate.setName(template.getName());
            existingTemplate.setContent(template.getContent());
            repository.save(existingTemplate);
        }
    }

    public List<PdfTemplate> getAllTemplates() {
        return repository.findAll();
    }
}
