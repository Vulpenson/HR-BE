package BE.artifact.service.impl;

import BE.artifact.model.Onboarding;
import BE.artifact.model.User;
import BE.artifact.payload.request.SignUpRequest;
import BE.artifact.model.UserRole;
import BE.artifact.payload.request.SignInRequest;
import BE.artifact.payload.response.JwtAuthenticationResponse;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.AuthenticationService;
import BE.artifact.service.JwtService;
import BE.artifact.service.OnboardingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = Logger.getLogger(AuthenticationServiceImpl.class.getName());
    private final OnboardingService onboardingService;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("User already exists");
            }

            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();
            userRepository.save(user);

            onboardingService.startOnboardingForUser(request.getEmail());

            var jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (InternalAuthenticationServiceException e) {
            logger.severe("Authentication service failed: %s".formatted(e.getMessage()));
            throw new AuthenticationServiceException("Authentication failed due to internal error");
        }
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse deleteUserById(Long id) {
        User user = userRepository.findById(id.toString()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.deleteByEmail(user.getEmail());
        return JwtAuthenticationResponse.builder().token("User deleted").build();
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.deleteByEmail(user.getEmail());
        return JwtAuthenticationResponse.builder().token("User deleted").build();
    }
}