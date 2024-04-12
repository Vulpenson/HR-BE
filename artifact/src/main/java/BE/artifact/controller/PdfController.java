package BE.artifact.controller;

import BE.artifact.service.PdfFormFillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfFormFillerService pdfFormFillerService;

    @GetMapping("/download-filled-form")
    public ResponseEntity<byte[]> downloadFilledForm() {
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("name", "John Doe");
            formData.put("date", "2021-09-01");
            byte[] pdfBytes = pdfFormFillerService.fillPdfTemplate("path/to/template.pdf", formData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "filled_form.pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}