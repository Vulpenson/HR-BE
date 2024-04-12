package BE.artifact.controller;

import BE.artifact.model.Payroll;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getYourPayrolls() {
        return ResponseEntity.ok(payrollService.getYourPayrolls());
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
    public ResponseEntity<?> getPayrollsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(payrollService.getPayrollsByEmail(email));
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