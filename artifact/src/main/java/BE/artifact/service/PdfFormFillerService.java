package BE.artifact.service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfFormFillerService {

    public byte[] fillPdfTemplate(String src, Map<String, String> formData) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(src);
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        formData.forEach((field, value) -> {
            PdfFormField formField = form.getField(field);
            if (formField != null) {
                formField.setValue(value);
            }
        });

        form.flattenFields();
        pdfDoc.close();
        writer.close();
        reader.close();

        return baos.toByteArray();
    }
}
