package com.thong.feature.employee.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequest(

    @NotNull(message = "User ID is required")
    Integer userId,

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    String lastName,

    String department,
    String position,

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be positive")
    BigDecimal baseSalary,

    LocalDate hireDate
) {}