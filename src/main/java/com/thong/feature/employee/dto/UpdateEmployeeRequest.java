package com.thong.feature.employee.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateEmployeeRequest(

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    String department,
    String position,

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be positive")
    BigDecimal baseSalary
) {}