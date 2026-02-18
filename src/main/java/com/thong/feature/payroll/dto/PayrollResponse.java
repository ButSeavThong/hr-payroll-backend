package com.thong.feature.payroll.dto;

import java.math.BigDecimal;

public record PayrollResponse(
    Integer id,
    Integer employeeId,
    String employeeName,
    String month,
    BigDecimal baseSalary,
    BigDecimal overtimePay,
    BigDecimal tax,
    BigDecimal netSalary,
    String status
) {}