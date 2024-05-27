package BE.artifact.service;

import BE.artifact.dto.UserDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDetailsService userDetailsService();
    UserDTO getUserByEmail(String email);
    Page<UserDTO> getAllUsers(Pageable pageable);
    void deleteUserByEmail(String email);
    void uploadCV(String email, MultipartFile cv);
    Resource downloadCV(String email);
}
