package BE.artifact.service;

import BE.artifact.dto.PayrollDTO;
import BE.artifact.model.Payroll;
import BE.artifact.model.User;
import BE.artifact.model.absence.Absence;
import BE.artifact.model.absence.AbsenceType;
import BE.artifact.repository.PayrollRepository;
import BE.artifact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;

    public Page<PayrollDTO> getPayrollsByEmail(String email, Pageable pageable) {
        Page<Payroll> payrollPage = payrollRepository.findByUserEmail(email, pageable);
        return payrollPage.map(PayrollDTO::from);
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
        Pageable pageable = Pageable.unpaged();
        List<Payroll> payrolls = payrollRepository.findByUserEmail(currentEmail, pageable).toList();

        return payrolls.stream()
                .max(Comparator.comparing(Payroll::getPayDate))
                .orElse(null);
    }

    public Page<PayrollDTO> getYourPayrolls(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        Page<Payroll> payrollPage = payrollRepository.findByUserEmail(currentEmail, pageable);
        return payrollPage.map(PayrollDTO::from);
    }

    public void savePayroll(String Email, PayrollDTO payrollDTO) {
        Payroll payroll = new Payroll();
        payroll.setPayDate(payrollDTO.getPayDate());
        payroll.setNetPay(payrollDTO.getNetPay());
        payroll.setUser(userRepository.findByEmail(Email)
                .orElseThrow(() -> new RuntimeException("User not found")));
        payrollRepository.save(payroll);
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

    public void payAll() {
        userRepository.findAll().forEach(user -> {
            Payroll payroll = new Payroll();
            payroll.setUser(user);
            payroll.setPayDate(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)));
            payroll.setNetPay(calculateSalary(user));
            payrollRepository.save(payroll);
        });
    }

    public Double calculateSalary(User user) {
        Double salary = user.getGrossPay();
        Double CAS = 0.2;
        Double CASS = 0.15;
        List<Absence> absences = user.getAbsences();
        for (Absence absence : absences) {
            if (absence.getType() == AbsenceType.UNPAID_LEAVE) {
                salary -= (absence.getEndDate().getDayOfYear() - absence.getStartDate().getDayOfYear()) * salary / 20;
            }
        }
        return salary - salary * (CAS + CASS);
    }

    public void setGrossPay(String email, Double amount) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setGrossPay(amount);
        userRepository.save(user);
    }
}
