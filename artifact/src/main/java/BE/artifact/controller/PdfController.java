package BE.artifact.controller;

import BE.artifact.service.PdfFormFillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfFormFillerService pdfFormFillerService;

    @GetMapping("/fill-template/{templateId}")
    public ResponseEntity<byte[]> fillPdfTemplate(@PathVariable Long templateId, @RequestParam String reason) {
        try {
            byte[] pdfBytes = pdfFormFillerService.fillPdfTemplate(templateId, reason);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filled_template.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}