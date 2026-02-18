package com.thong.feature.attendance.dto;

import java.time.LocalDate;

// Employee identity comes from JWT — not from the request body
public record CheckInRequest(
    LocalDate date   // null = defaults to today, handled in service
) {}