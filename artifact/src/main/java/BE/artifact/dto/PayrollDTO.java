package BE.artifact.dto;

import BE.artifact.model.Payroll;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PayrollDTO {
    private Date payDate;
    private Double netPay;

    public static PayrollDTO from(Payroll payroll) {
        return new PayrollDTO(payroll.getPayDate(), payroll.getNetPay());
    }
}
