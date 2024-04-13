package BE.artifact.controller;

import BE.artifact.model.Payroll;
import BE.artifact.model.User;
import BE.artifact.model.UserRole;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.PayrollService;
import BE.artifact.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PayrollControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayrollService payrollService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtServiceImpl jwtService;

    List<Payroll> payrolls = new ArrayList<>();
    private String token;

    @BeforeEach
    public void setup() {

//        for (long i = 0; i < 7; i++) {
//            payrolls.add(PayrollUtilsTest.createPayroll(i));
//        }

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        user.setRole(UserRole.ROLE_ADMIN);

        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
//        when(jwtService.generateToken(any(User.class))).thenReturn(jwtService.generateToken(userDetails));
        token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcxMjU5Mjg1OSwiZXhwIjoxNzEyNTk0Mjk5fQ.K569dq2YlJAh1h_x9bOCHzfIsX8xZlLBScq2qsrxqxg";
    }

    @Test
    public void getAllPayrollsTest() throws Exception {
        when(payrollService.getAllPayrolls()).thenReturn(payrolls);

        mockMvc.perform(post("/api/payroll/all")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getYourLastPayrollTest() throws Exception {
        when(payrollService.getYourLastPayroll()).thenReturn(payrolls.get(0));


        mockMvc.perform(post("/api/payroll/last")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        // print all the payrolls
        when(payrollService.getAllPayrolls()).thenReturn(payrolls);
        mockMvc.perform(post("/api/payroll/all")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deletePayrollTest() throws Exception {
        mockMvc.perform(post("/api/payroll/delete/{id}", 1L)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assert(payrollService.getPayrollById(1L) == null);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deletePayrollsByEmailTest() throws Exception {
        mockMvc.perform(post("/api/payroll/delete-all/{email}", "test@test.com")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    public void savePayrollTest() throws Exception {
//        when(payrollService.savePayroll(anyString(), any(Payroll.class))).thenReturn(payrolls.get(0));
//
//        mockMvc.perform(post("/api/payroll/save/{email}", "test@test.com")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(payrolls.get(0))))
//                .andExpect(status().isOk());
//    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getPayrollsByEmailTest() throws Exception {
        when(payrollService.getPayrollsByEmail(anyString())).thenReturn(payrolls);

        mockMvc.perform(get("/api/payroll/user/{email}", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getPayrollByIdTest() throws Exception {
        when(payrollService.getPayrollById(anyLong())).thenReturn(payrolls.get(0));

        mockMvc.perform(get("/api/payroll/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}