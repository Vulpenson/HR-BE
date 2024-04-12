package BE.artifact.integration;

import BE.artifact.model.Payroll;
import BE.artifact.payload.request.SignInRequest;
import BE.artifact.service.PayrollService;
import BE.artifact.utils.PayrollUtilsTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PayrollControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PayrollService payrollService;

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
    public void whenGetAllPayrolls_thenReturns200() throws Exception {

        mockMvc.perform(get("/api/payroll/all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void whenGetYourLastPayroll_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/payroll/last")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()); // Assuming your Payroll object has an 'id' field
    }

    @Test
    @Disabled("This test will fail if there are no payrolls to delete")
    public void whenDeletePayroll_thenReturns200() throws Exception {
        // Assuming you have a payroll with ID 1 for deletion in setup()
        mockMvc.perform(post("/api/payroll/delete/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Payroll deleted"));
    }

    @Test
    @Disabled
    public void whenUpdatePayroll_thenReturns200() throws Exception {
        // Assuming you have a payroll with ID 1 for update in setup()
        mockMvc.perform(post("/api/payroll/update/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PayrollUtilsTest.createPayroll(2L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()); // Assuming your Payroll object has an 'id' field
    }

    @Test
    public void whenGetPayrollById_thenReturns200() throws Exception {
        // Assuming you have a payroll with ID 1 for retrieval in setup()
        mockMvc.perform(get("/api/payroll/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()); // Assuming your Payroll object has an 'id' field
    }

    @Test
    public void whenCreatePayroll_thenReturns200() throws Exception {
        Random random = new Random();
        mockMvc.perform(post("/api/payroll/save/test@gmail.com")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PayrollUtilsTest.createPayroll(random.nextLong()))))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").exists()); // Assuming your Payroll object has an 'id' field
    }

    @Test
    public void whenGetPayrollsByEmail_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/user/test@gmail.com")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Disabled("This test will fail if there are no payrolls to delete")
    public void whenDeletePayrollsByEmail_thenReturns200() throws Exception {
        mockMvc.perform(post("/delete-all/test@gmail.com")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Payrolls deleted"));
    }

    @Test
    public void whenPayAll_thenReturns200() throws Exception {
        mockMvc.perform(post("/api/payroll/pay/all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All employees paid"));
    }

    @Test
    public void whenGetYourPayrolls_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/payroll/user/all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$").isArray());
    }
}
