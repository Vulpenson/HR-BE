package BE.artifact.integration;

import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import BE.artifact.payload.request.SignInRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testSubmitFeedback() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setFeedback("Good job!");
        feedback.setFeedbackType(FeedbackType.OTHER);
        mockMvc.perform(post("/api/feedback/submit")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Feedback submitted successfully")));
    }

    @Test
    public void testSubmitFeedbackInvalid() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setFeedback("Good shit!");
        feedback.setFeedbackType(FeedbackType.OTHER);
        mockMvc.perform(post("/api/feedback/submit")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid feedback provided")));
    }

    @Test
    public void testGetAllFeedback() throws Exception {
        mockMvc.perform(get("/api/feedback/all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Disabled
    public void testBulkSubmitFeedback() throws Exception {
        FeedbackType[] types = FeedbackType.values();
        int numberOfFeedbacks = 50;  // Total number of feedback entries to submit

        for (int i = 0; i < numberOfFeedbacks; i++) {
            Feedback feedback = new Feedback();
            // Cycle through FeedbackType values for each new feedback
            FeedbackType type = types[i % types.length];
            feedback.setFeedback("Feedback for " + type.name() + " " + i);
            feedback.setFeedbackType(type);

            mockMvc.perform(post("/api/feedback/submit")
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(feedback)))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Test
    public void testGetFeedbackByType() throws Exception {
        mockMvc.perform(get("/api/feedback/type/BEHAVIOR")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
