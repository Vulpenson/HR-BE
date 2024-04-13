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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfFormFillerService {

    @Autowired
    private PdfTemplateRepository pdfTemplateRepository;

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

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

    public byte[] fillPdfTemplate(Long templateId) throws Exception {
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

        Map<String, String> formData = Map.of(
                "name", details.getUser().getFirstName(),
                "email", details.getUser().getEmail(),
                "cnp", details.getCNP(),
                "phone", details.getPhoneNumber(),
                "address", details.getAddress(),
                "city", details.getCity(),
                "country", details.getCountry(),
                "postal_code", details.getPostalCode(),
                "bank", details.getBank(),
                "bank_account", details.getBankAccount()
        );

        formData.put("identity_card", details.getIdentityCard());
        formData.put("identity_card_series", details.getIdentityCardSeries());
        formData.put("identity_card_number", details.getIdentityCardNumber());
        formData.put("registered_by", details.getRegisteredBy());
        formData.put("registration_date", details.getRegistrationDate());
        formData.put("company_position", details.getCompanyPosition());
        formData.put("contract_number", details.getContractNumber());
        formData.put("contract_start_date", details.getContractStartDate());

        formData.forEach((key, value) -> {
            PdfFormField field = form.getField(key);
            if (field != null) {
                field.setValue(value);
            }
        });

        form.flattenFields();
        pdfDoc.close();
        return baos.toByteArray();
    }
}
