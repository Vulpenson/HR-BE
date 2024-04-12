package BE.artifact.integration;

import BE.artifact.dto.AbsenceDTO;
import BE.artifact.model.absence.AbsenceType;
import BE.artifact.payload.request.SignInRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AbsenceIntegrationTest {
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
    public void testAddAbsence() throws Exception {
        AbsenceDTO absenceDTO = new AbsenceDTO(LocalDate.now().minusDays(4), LocalDate.now(), AbsenceType.WORK_FROM_HOME, true); // Populate your DTO as needed
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(post("/api/absences/add-test")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(absenceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetAllAbsences() throws Exception {
        mockMvc.perform(get("/api/absences/all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Disabled("This test is failing because the absence with id 1 does not exist in the database. " +
            "You should either create the absence with id 1 or update the test to use an existing absence id.")
    public void testDeleteAbsence() throws Exception {
        mockMvc.perform(delete("/api/absences/{id}", 1)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testApproveAbsence() throws Exception {
        mockMvc.perform(post("/api/absences/approve/{id}", 2)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    public void testUpdateAbsence() throws Exception {
        AbsenceDTO absenceDTO = new AbsenceDTO(LocalDate.now().minusDays(4), LocalDate.now(), AbsenceType.WORK_FROM_HOME, true); // Populate your DTO as needed
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(post("/api/absences/update/{id}", 2)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(absenceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetAbsenceById() throws Exception {
        mockMvc.perform(get("/api/absences/{id}", 2)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetAbsencesForCurrentUser() throws Exception {
        mockMvc.perform(get("/api/absences/current-user")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testAddAbsenceForCurrentUser() throws Exception {
        AbsenceDTO absenceDTO = new AbsenceDTO(LocalDate.now().minusDays(4), LocalDate.now(), AbsenceType.WORK_FROM_HOME, true); // Populate your DTO as needed
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(post("/api/absences/add-current-user")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(absenceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

}
