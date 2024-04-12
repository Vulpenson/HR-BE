package BE.artifact.utils;

import BE.artifact.model.Payroll;

import java.time.ZoneOffset;
import java.util.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;

public class PayrollUtilsTest {
    public static Payroll createPayroll(Long id) {
        Payroll payroll = new Payroll();

        Random random = new Random();
        payroll.setId(id);
        payroll.setPayDate(subtractMonths(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)), random.nextInt(30)));
//        payroll.setGrossPay(1000.0);
//        payroll.setDeductions(200.0);
//        payroll.setNetPay(800.0);

        return payroll;
    }

    public static Date subtractMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -months); // Subtract months
        return calendar.getTime();
    }
}
