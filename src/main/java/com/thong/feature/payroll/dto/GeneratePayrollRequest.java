package com.thong.feature.payroll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GeneratePayrollRequest(

    Integer employeeId,  // null = generate for ALL active employees

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "Month must be in format yyyy-MM")
    String month
) {}