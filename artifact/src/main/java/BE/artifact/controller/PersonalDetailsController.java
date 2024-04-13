package BE.artifact.controller;

import BE.artifact.model.PersonalDetails;
import BE.artifact.service.PersonalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-details")
@RequiredArgsConstructor
public class PersonalDetailsController {
    private final PersonalDetailsService personalDetailsService;

    @GetMapping("/current")
    public ResponseEntity<?> getPersonalDetailsOfCurrentUser() {
        return ResponseEntity.ok(personalDetailsService.getPersonalDetailsOfCurrentUser());
    }

    @DeleteMapping("/current/delete")
    public ResponseEntity<?> deletePersonalDetailsOfCurrentUser() {
        personalDetailsService.deletePersonalDetailsOfCurrentUser();
        return ResponseEntity.ok("Personal details deleted");
    }

    @PostMapping("/current/update")
    public ResponseEntity<?> updatePersonalDetailsOfCurrentUser(@RequestBody PersonalDetails personalDetails) {
        personalDetailsService.updatePersonalDetailsOfCurrentUser(personalDetails);
        return ResponseEntity.ok("Personal details updated");
    }

    @PostMapping("/current/save")
    public ResponseEntity<?> savePersonalDetailsOfCurrentUser(@RequestBody PersonalDetails personalDetails) {
        personalDetailsService.savePersonalDetailsOfCurrentUser(personalDetails);
        return ResponseEntity.ok("Personal details saved");
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getPersonalDetailsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(personalDetailsService.getPersonalDetailsByEmail(email));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePersonalDetailsById(@PathVariable Integer id) {
        personalDetailsService.deletePersonalDetailsById(id);
        return ResponseEntity.ok("Personal details deleted");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable Integer id, @RequestBody PersonalDetails personalDetails) {
        personalDetailsService.updatePersonalDetails(id, personalDetails);
        return ResponseEntity.ok("Personal details updated");
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePersonalDetails(@RequestBody PersonalDetails personalDetails) {
        personalDetailsService.savePersonalDetails(personalDetails);
        return ResponseEntity.ok("Personal details saved");
    }
}
