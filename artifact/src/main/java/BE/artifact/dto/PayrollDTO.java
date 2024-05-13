package BE.artifact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PayrollDTO {
    private Date payDate;
    private Double netPay;

    public PayrollDTO from(Date payDate, Double netPay) {
        return new PayrollDTO(payDate, netPay);
    }
}
