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
        logger.info("User signed in successfully");
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            authenticationService.deleteUser(id);
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
