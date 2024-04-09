package BE.artifact.controller;

import BE.artifact.model.User;
import BE.artifact.model.UserRole;
import BE.artifact.payload.request.SignInRequest;
import BE.artifact.payload.request.SignUpRequest;
import BE.artifact.payload.response.JwtAuthenticationResponse;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.AuthenticationService;
import BE.artifact.service.JwtService;
import BE.artifact.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private org.springframework.web.context.WebApplicationContext context;

    Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .defaultRequest(get("/").with(csrf())) // Use this to include CSRF token
                .build();
    }

    @Test
    public void testSignup() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("test");
        request.setRole(UserRole.ROLE_ADMIN);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("token");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testSignin() throws Exception {
        SignInRequest request = new SignInRequest("test@gmail.com", "test");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        logger.info(json);

        String expectedToken = "mockedJwtTokenHere";
        when(authenticationService.signin(request))
                .thenReturn(JwtAuthenticationResponse.builder().token(expectedToken).build()); // Mock the authentication logic to return a fixed token


        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    // Here's how you can log just the response body using your logger
                    String responseBody = result.getResponse().getContentAsString();
                    logger.info(responseBody);
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/auth/delete/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testSignOut() throws Exception {
        mockMvc.perform(post("/api/auth/signout"))
                .andExpect(status().isOk());
    }
}