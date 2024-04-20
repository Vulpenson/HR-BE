package BE.artifact.integration;

import BE.artifact.model.UserRole;
import BE.artifact.payload.request.SignInRequest;
import BE.artifact.payload.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenSignInWithValidCredentials_thenGeneratesJwtToken() throws Exception {
        // Assuming you have a user "user@example.com" with password "password"
        SignInRequest signInRequest = new SignInRequest("test@gmail.com", "test");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue())) // Check that a token is returned
                .andExpect(jsonPath("$.token", matchesPattern("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"))); // Basic regex for JWT structure
    }

    @Test
    public void whenSignInWithInvalidCredentials_thenReturnsAccessDenied() throws Exception {
        SignInRequest signInRequest = new SignInRequest("invalid", "invalid");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenSignUpWithValidCredentials_thenReturnsJwtToken() throws Exception {
        SignInRequest signInRequest = new SignInRequest("test@gmail.com", "test");
        String adminToken = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andReturn().getResponse().getContentAsString();
        adminToken = "Bearer " + new ObjectMapper().readTree(adminToken).get("token").textValue();

        SignUpRequest signUpRequest = new SignUpRequest("Alex", "Vlad", "tester@test.com", UserRole.ROLE_EMPLOYEE, "test");

        mockMvc.perform(post("/api/auth/signup")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    public void deleteUser() throws Exception {
        SignInRequest signInRequest = new SignInRequest("test@gmail.com", "test");
        String adminToken = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andReturn().getResponse().getContentAsString();
        adminToken = "Bearer " + new ObjectMapper().readTree(adminToken).get("token").textValue();

        mockMvc.perform(delete("/api/auth/delete/tester@test.com")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
