package BE.artifact.controller;

import BE.artifact.dto.UserDTO;
import BE.artifact.service.AuthenticationService;
import BE.artifact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @PostMapping("/upload-cv")
    public ResponseEntity<String> uploadCv(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.uploadCV(email, file);
        return ResponseEntity.ok("CV uploaded successfully");
    }

    @GetMapping("/download-cv")
    public ResponseEntity<Resource> downloadCv() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Resource file = userService.downloadCV(email);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/subordinates")
    public ResponseEntity<List<String>> getSubordinates() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            List<String> subordinatesEmails = userService.getSubordinatesEmails(email);
            return ResponseEntity.ok(subordinatesEmails);
        } catch (Exception e) {
            logger.info("Error getting subordinates");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/set-manager")
    @PreAuthorize("hasRole('ROLE_HR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> setManager(@RequestParam String userEmail, @RequestParam String managerEmail) {
        try {
            userService.setManager(userEmail, managerEmail);
            return ResponseEntity.ok("Manager set successfully");
        } catch (Exception e) {
            logger.info("Error setting manager");
            throw new RuntimeException(e);
        }
    }
}
