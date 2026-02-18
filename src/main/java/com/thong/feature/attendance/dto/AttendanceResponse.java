package com.thong.feature.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceResponse(
    Integer id,
    Integer employeeId,
    String employeeName,
    LocalDate date,
    LocalDateTime checkInTime,
    LocalDateTime checkOutTime,
    Double totalHours,
    Double overtimeHours
) {}