package BE.artifact.integration;

import BE.artifact.model.PersonalDetails;
import BE.artifact.payload.request.SignInRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonalDetailsIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

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
    public void getPersonalDetailsOfCurrentUser() throws Exception {
        mockMvc.perform(get("/api/personal-details/current")
                        .header("Authorization", adminToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    public void deletePersonalDetailsOfCurrentUser() throws Exception {
        mockMvc.perform(delete("/api/personal-details/current/delete")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().string("Personal details deleted"));
    }

    @Test
    public void updatePersonalDetailsOfCurrentUser() throws Exception {
        PersonalDetails details = personalDetailsNewTest(); // Construct your details object here
        mockMvc.perform(post("/api/personal-details/current/update")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(content().string("Personal details updated"));
    }

    @Test
    public void savePersonalDetailsOfCurrentUser() throws Exception {
        PersonalDetails details = personalDetailsNewTest();
        mockMvc.perform(post("/api/personal-details/current/save")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().string("Personal details saved"));
    }

    @Test
    public void getPersonalDetailsByEmail() throws Exception {
        String email = "test@gmail.com";
        mockMvc.perform(get("/api/personal-details/email/{email}", email)
                        .header("Authorization", adminToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void deletePersonalDetailsById() throws Exception {
        Integer id = 1;
        mockMvc.perform(delete("/api/personal-details/delete/{id}", id)
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Personal details deleted"));
    }

    @Test
    public void updatePersonalDetails() throws Exception {
        Integer id = 1;
        PersonalDetails details = personalDetailsNewTest(); // Construct your details object here
        mockMvc.perform(post("/api/personal-details/update/{id}", id)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(content().string("Personal details updated"));
    }

    @Test
    public void savePersonalDetails() throws Exception {
        PersonalDetails details = personalDetailsNewTest(); // Construct your details object here
        mockMvc.perform(post("/api/personal-details/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(content().string("Personal details saved"));
    }

    public static PersonalDetails personalDetailsNewTest() {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setCNP("5020326134139");
        personalDetails.setPhoneNumber("0723456789");
        personalDetails.setAddress("Str. Test, Nr. 1");
        personalDetails.setCity("Test City");
        personalDetails.setCountry("Test Country");
        personalDetails.setPostalCode("123456");
        personalDetails.setBank("Test Bank");
        personalDetails.setBankAccount("RO123456789");
        personalDetails.setIdentityCard("Test Identity Card");
        personalDetails.setIdentityCardSeries("AB");
        personalDetails.setIdentityCardNumber("123456");
        personalDetails.setRegisteredBy("Test User");
        personalDetails.setRegistrationDate("2021-01-01");
        personalDetails.setCompanyPosition("Test Position");
        personalDetails.setContractNumber("123456");
        personalDetails.setContractStartDate("2021-01-01");
        return personalDetails;
    }
}
