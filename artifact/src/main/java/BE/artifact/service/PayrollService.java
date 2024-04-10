package BE.artifact.service;

import BE.artifact.model.Payroll;
import BE.artifact.repository.PayrollRepository;
import BE.artifact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;

    public List<Payroll> getPayrollsByEmail(String email) {
        return payrollRepository.findByUserEmail(email);
    }

    public Payroll getPayrollById(Long id) {
        return payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id " + id));
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    public Payroll getYourLastPayroll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        List<Payroll> payrolls = payrollRepository.findByUserEmail(currentEmail);

        return payrolls.stream()
                .max(Comparator.comparing(Payroll::getPayDate))
                .orElse(null);
    }

    public Payroll savePayroll(String Email, Payroll payroll) {
        payroll.setUser(userRepository.findByEmail(Email)
                .orElseThrow(() -> new RuntimeException("User not found")));
        return payrollRepository.save(payroll);
    }

    public void deletePayroll(Long id) {
        payrollRepository.deleteById(id);
    }

    public void deletePayrollsByEmail(String email) {
        payrollRepository.deleteByUserEmail(email);
    }

    public Payroll updatePayroll(Long id, Payroll payroll) {
        Payroll existingPayroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id " + id));
        existingPayroll.setPayDate(payroll.getPayDate());
        return payrollRepository.save(existingPayroll);
    }
}
