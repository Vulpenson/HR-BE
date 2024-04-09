package BE.artifact.controller;

import BE.artifact.payload.request.SignInRequest;
import BE.artifact.payload.request.SignUpRequest;
import BE.artifact.payload.response.JwtAuthenticationResponse;
import BE.artifact.service.AuthenticationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        try {
            JwtAuthenticationResponse response = authenticationService.signup(request);
            logger.info("User signed up successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.info("User already exists");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.signin(request));
        } catch (Exception e) {
            logger.info("Invalid credentials");
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            authenticationService.deleteUserById(id);
            logger.info("User deleted successfully");
        } catch (Exception e) {
            logger.info("User not found");
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("User deleted");
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        try {
            authenticationService.deleteUserByEmail(email);
            logger.info("User deleted successfully");
        } catch (Exception e) {
            logger.info("User not found");
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("User deleted");
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("User signed out successfully");
    }
}
