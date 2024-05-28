package BE.artifact.service;

import BE.artifact.model.PdfTemplate;
import BE.artifact.model.PersonalDetails;
import BE.artifact.repository.PdfTemplateRepository;
import BE.artifact.repository.PersonalDetailsRepository;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PdfFormFillerService {

    @Autowired
    private PdfTemplateRepository pdfTemplateRepository;

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    Logger logger = Logger.getLogger(PdfFormFillerService.class.getName());

    public byte[] generatePersonalDetailsDocument() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        PersonalDetails details = personalDetailsRepository.findByUserEmail(currentEmail);
        if (details == null) {
            throw new IllegalStateException("No personal details found for user ID: " + currentEmail);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Personal Details for " + details.getUser().getEmail()));
        document.add(new Paragraph("CNP: " + details.getCNP()));
        document.add(new Paragraph("Phone Number: " + details.getPhoneNumber()));
        document.add(new Paragraph("Address: " + details.getAddress()));
        // Add other details similarly

        document.close();

        return baos.toByteArray();
    }

    @Transactional
    public byte[] fillPdfTemplate(Long templateId, String reason) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        PdfTemplate template = pdfTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        PersonalDetails details = personalDetailsRepository.findByUserEmail(currentEmail);

        PdfReader reader = new PdfReader(new ByteArrayInputStream(template.getContent()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Map<String, String> formData = getStringStringMap(reason, details);

        formData.forEach((key, value) -> {
            PdfFormField field = form.getField(key);
            if (field != null) {
                field.setValue(value);
            }
        });

        logger.severe(formData.toString());;

        form.flattenFields();
        pdfDoc.close();
        return baos.toByteArray();
    }

    private static Map<String, String> getStringStringMap(String reason, PersonalDetails details) {
        String name = details.getUser().getFirstName() + " " + details.getUser().getLastName();
        String grossPay = details.getUser().getGrossPay().toString();

        Map<String, String> formData = new HashMap<>();
        formData.put("name", name);
        formData.put("address", details.getAddress());
        formData.put("cnp", details.getCNP());
        formData.put("identityCard", details.getIdentityCard());
        formData.put("identityCardSeries", details.getIdentityCardSeries());
        formData.put("identityCardNumber", details.getIdentityCardNumber());
        formData.put("registeredBy", details.getRegisteredBy());
        formData.put("registrationDate", details.getRegistrationDate());
        formData.put("contractStartDate", details.getContractStartDate());
        formData.put("companyPosition", details.getCompanyPosition());
        formData.put("grossPay", grossPay);
        formData.put("contractNumber", details.getContractNumber());
        formData.put("reason", reason);
        return formData;
    }
}
