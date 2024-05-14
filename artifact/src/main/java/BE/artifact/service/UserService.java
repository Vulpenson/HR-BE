package BE.artifact.service;

import BE.artifact.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    UserDTO getUserByEmail(String email);
}
