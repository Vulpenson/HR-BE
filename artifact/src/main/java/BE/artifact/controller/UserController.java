package BE.artifact.controller;

import BE.artifact.dto.UserDTO;
import BE.artifact.service.AuthenticationService;
import BE.artifact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_HR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String dir) {
        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(dir);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            Page<UserDTO> userDTOsPage = userService.getAllUsers(pageable);
            return ResponseEntity.ok(userDTOsPage);
        } catch (Exception e) {
            logger.info("Error getting all users");
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok("User deleted");
        } catch (Exception e) {
            logger.info("Error deleting user");
            throw new RuntimeException(e);
        }
    }
}
