package BE.artifact.controller;

import BE.artifact.dto.JobOfferDTO;
import BE.artifact.model.recruiting.JobOffer;
import BE.artifact.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
@RequiredArgsConstructor
public class JobOfferController {
    private final JobOfferService jobOfferService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<JobOffer> createJobOffer(@RequestBody JobOfferDTO jobOffer) {
        JobOffer newJobOffer = jobOfferService.createJobOffer(jobOffer);
        return ResponseEntity.ok(newJobOffer);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable Long id) {
        JobOffer jobOffer = jobOfferService.getJobOfferById(id);
        return ResponseEntity.ok(jobOffer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public ResponseEntity<?> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.ok("Job offer deleted");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable Long id, @RequestBody JobOffer jobOffer) {
        JobOffer updatedJobOffer = jobOfferService.updateJobOffer(id, jobOffer);
        return ResponseEntity.ok(updatedJobOffer);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<JobOfferDTO>> getActiveJobOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String dir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sortBy));
        Page<JobOfferDTO> jobOffersPage = jobOfferService.getActiveJobOffers(pageable);
        return ResponseEntity.ok(jobOffersPage);
    }
}