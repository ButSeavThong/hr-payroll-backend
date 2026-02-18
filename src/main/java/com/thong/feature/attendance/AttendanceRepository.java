package com.thong.feature.attendance;

import com.thong.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    // Find today's record — used during check-out
    Optional<Attendance> findByEmployeeIdAndDate(Integer employeeId, LocalDate date);

    // Employee views own history
    List<Attendance> findByEmployeeId(Integer employeeId);

    // Used by Payroll to sum overtime hours for a given month
    List<Attendance> findByEmployeeIdAndDateBetween(
        Integer employeeId, LocalDate start, LocalDate end);
}