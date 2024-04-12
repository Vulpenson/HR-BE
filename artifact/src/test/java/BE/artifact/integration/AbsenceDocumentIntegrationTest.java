package BE.artifact.integration;

import BE.artifact.model.absence.AbsenceDocument;
import BE.artifact.payload.request.SignInRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AbsenceDocumentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String adminToken;

    @BeforeEach
    public void setup() throws Exception {
        // Sign in as an admin user and get the token
        SignInRequest signInRequest = new SignInRequest("test@gmail.com", "test");
        adminToken = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andReturn().getResponse().getContentAsString();
        adminToken = "Bearer " + new ObjectMapper().readTree(adminToken).get("token").textValue();
    }

    @Test
    public void testUploadDocument() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "application/pdf", "some xml".getBytes());
        mockMvc.perform(multipart("/api/absence-documents/upload/2").file(file)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Document stored with ID:")));
    }

    @Test
    public void testGetDocument() throws Exception {
        mockMvc.perform(get("/api/absence-documents/{id}", 2)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(header().string("Content-Type", "application/pdf"));  // Assuming PDF files
    }

    @Test
    @Disabled("This test will fail if the document with ID 1 does not exist")
    public void testDeleteDocument() throws Exception {
        mockMvc.perform(delete("/api/absence-documents/{id}", 1)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Document deleted"));
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        mockMvc.perform(get("/api/absence-documents/all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDocumentsForAbsence() throws Exception {
        mockMvc.perform(get("/api/absence-documents/absence/{absenceId}", 2)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled("This test will fail if the absence with ID 1 does not exist")
    public void testDeleteDocumentsForAbsence() throws Exception {
        mockMvc.perform(delete("/api/absence-documents/absence/{absenceId}", 1)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Documents deleted"));
    }

    @Test
    public void testUpdateDocument() throws Exception {
        AbsenceDocument document = new AbsenceDocument();  // Setup document details
        document.setName("UpdatedName.pdf");
        mockMvc.perform(patch("/api/absence-documents/{id}", 1)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isOk());
    }
}
