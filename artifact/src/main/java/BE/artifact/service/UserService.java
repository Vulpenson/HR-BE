package BE.artifact.service;

import BE.artifact.dto.UserDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    UserDTO getUserByEmail(String email);
    Page<UserDTO> getAllUsers(Pageable pageable);
    void deleteUserByEmail(String email);
    void uploadCV(String email, MultipartFile cv);
    Resource downloadCV(String email);
    void updateUser(UserDTO userDTO);
    void updatePassword(String email, String password);
    List<String> getSubordinatesEmails(String email);
    void setManager(String email, String managerEmail);
    void sendPasswordResetToken(String email);
    void sendResetEmail(String email, String token);
    void resetPassword(String email, String password);
}
