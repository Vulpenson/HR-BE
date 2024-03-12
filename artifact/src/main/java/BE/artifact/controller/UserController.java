package BE.artifact.controller;

import BE.artifact.model.Roles;
import BE.artifact.model.User;
import BE.artifact.model.UserRole;
import BE.artifact.payload.request.LoginRequest;
import BE.artifact.payload.request.SignUpRequest;
import BE.artifact.payload.response.JwtResponse;
import BE.artifact.payload.response.MessageResponse;
import BE.artifact.repository.RolesRepository;
import BE.artifact.repository.UserRepository;
import BE.artifact.security.jwt.JwtUtil;
import BE.artifact.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final RolesRepository roleRepository;
    private final PasswordEncoder encoder;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final String jwt = jwtUtil.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        System.out.println("New user sign-in: " + loginRequest);
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));

    }
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getPhone(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        String strRoles = signUpRequest.getRole();
        Set<Roles> roles = new HashSet<>();

        if (strRoles == null) {
            Roles userRole = roleRepository.findByName(UserRole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            switch (strRoles) {
                case "ROLE_ADMIN" -> {
                    Roles adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }
                case "ROLE_HR" -> {
                    Roles hrRole = roleRepository.findByName(UserRole.ROLE_HR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(hrRole);
                }
                default -> {
                    Roles employeeRole = roleRepository.findByName(UserRole.ROLE_EMPLOYEE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(employeeRole);
                }
            }

        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}