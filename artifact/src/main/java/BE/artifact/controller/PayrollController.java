package BE.artifact.controller;

import BE.artifact.dto.PayrollDTO;
import BE.artifact.model.Payroll;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import BE.artifact.service.PayrollService;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPayrolls() {
        return ResponseEntity.ok(payrollService.getAllPayrolls());
    }

    @GetMapping("/last")
    public ResponseEntity<?> getYourLastPayroll() {
        return ResponseEntity.ok(payrollService.getYourLastPayroll());
    }

    @GetMapping("/user/all")
    public ResponseEntity<?> getYourPayrolls(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "payDate") String sortBy,
            @RequestParam(defaultValue = "desc") String dir
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<PayrollDTO> payrollPage = payrollService.getYourPayrolls(pageable);
        return ResponseEntity.ok(payrollPage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.ok("Payroll deleted");
    }

    @PostMapping("/delete-all/{email}")
    public ResponseEntity<?> deletePayrollsByEmail(@PathVariable String email) {
        payrollService.deletePayrollsByEmail(email);
        return ResponseEntity.ok("Payrolls deleted");
    }

    @PostMapping("/save/{email}")
    public ResponseEntity<?> savePayroll(@PathVariable String email, @RequestBody Payroll payroll) {
        return ResponseEntity.ok(payrollService.savePayroll(email, payroll));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getPayrollsByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "payDate") String sortBy,
            @RequestParam(defaultValue = "desc") String dir
            ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(payrollService.getPayrollsByEmail(email, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayrollById(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.getPayrollById(id));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePayroll(@PathVariable Long id, @RequestBody Payroll payroll) {
        return ResponseEntity.ok(payrollService.updatePayroll(id, payroll));
    }

    @PostMapping("/pay/all")
    public ResponseEntity<?> payAll() {
        try {
            payrollService.payAll();
            return ResponseEntity.ok("All employees paid");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/grosspay/{email}/{amount}")
    public ResponseEntity<?> setGrossPay(@PathVariable String email, @PathVariable Double amount) {
        try {
            payrollService.setGrossPay(email, amount);
            return ResponseEntity.ok("Gross pay set");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
