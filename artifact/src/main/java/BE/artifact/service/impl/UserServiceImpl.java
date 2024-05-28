package BE.artifact.service.impl;

import BE.artifact.dto.UserDTO;
import BE.artifact.mail.EmailService;
import BE.artifact.model.PasswordResetToken;
import BE.artifact.model.User;
import BE.artifact.model.UserRole;
import BE.artifact.repository.TokenRepository;
import BE.artifact.repository.UserRepository;
import BE.artifact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

//    @Value("${reset-password.base-url}")
    private final String resetPasswordBaseURL = "http://localhost:3000";

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDTO::from)
                .orElse(null);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDTO::from);
    }

    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional
    @Override
    public void uploadCV(String email, MultipartFile cv) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setCvContent(cv.getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public Resource downloadCV(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(value -> new ByteArrayResource(value.getCvContent())).orElse(null);
    }

    @Transactional
    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRole(UserRole.valueOf(userDTO.getRole()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public void setManager(String email, String managerEmail) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        user.setManager(manager);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void sendPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a reset token
        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        // Save the token in the database
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);
        resetToken.setUser(user);

        tokenRepository.save(resetToken);

        // Send email
        sendResetEmail(user.getEmail(), token);
    }

    @Override
    public void sendResetEmail(String email, String token) {
        String resetURL = resetPasswordBaseURL + "/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String message = "To reset your password, click the link below:\n" + resetURL;

        emailService.sendSimpleMessage(email, subject, message);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token or token expired"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Invalidate the token
        tokenRepository.delete(resetToken);
    }

    @Override
    public List<String> getSubordinatesEmails(String managerEmail) {
        return userRepository.findByManagerEmail(managerEmail)
                .stream()
                .map(User::getEmail)
                .toList();
    }
}