package com.thong.feature.payroll;

import com.thong.domain.Payroll;
import com.thong.feature.payroll.dto.PayrollResponse;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {

    public PayrollResponse toResponse(Payroll payroll) {
        String name = payroll.getEmployee().getFirstName()
            + " " + payroll.getEmployee().getLastName();
        return new PayrollResponse(
            payroll.getId(),
            payroll.getEmployee().getId(),
            name,
            payroll.getMonth(),
            payroll.getBaseSalary(),
            payroll.getOvertimePay(),
            payroll.getTax(),
            payroll.getNetSalary(),
            payroll.getStatus()
        );
    }
}