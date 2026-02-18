package com.thong.feature.attendance;

import com.thong.domain.Attendance;
import com.thong.feature.attendance.dto.AttendanceResponse;
import com.thong.feature.attendance.dto.CheckInRequest;
import com.thong.feature.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    private static final double STANDARD_HOURS = 8.0;

    @Override
    @Transactional
    public AttendanceResponse checkIn(Integer employeeId, CheckInRequest request) {
        LocalDate date = (request.date() != null) ? request.date() : LocalDate.now();

        // Business rule: only ONE check-in per employee per day
        if (attendanceRepository.findByEmployeeIdAndDate(employeeId, date).isPresent()) {
            throw new IllegalStateException("Already checked in for date: " + date);
        }

        var employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        var attendance = Attendance.builder()
            .employee(employee)
            .date(date)
            .checkInTime(LocalDateTime.now())
            .build();

        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut(Integer employeeId) {
        var attendance = attendanceRepository
            .findByEmployeeIdAndDate(employeeId, LocalDate.now())
            .orElseThrow(() -> new RuntimeException("No check-in found for today"));

        if (attendance.getCheckOutTime() != null) {
            throw new IllegalStateException("Already checked out today");
        }

        LocalDateTime checkOut = LocalDateTime.now();
        attendance.setCheckOutTime(checkOut);

        // Duration.between gives exact precision
        // toMinutes() / 60.0 gives fractional hours (e.g., 9h 30m = 9.5)
        Duration duration = Duration.between(attendance.getCheckInTime(), checkOut);
        double totalHours = duration.toMinutes() / 60.0;
        attendance.setTotalHours(totalHours);

        // Overtime = hours beyond 8. Math.max(0,...) ensures never negative.
        double overtime = Math.max(0, totalHours - STANDARD_HOURS);
        attendance.setOvertimeHours(overtime);

        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getMyAttendance(Integer employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId)
            .stream()
            .map(attendanceMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendance() {
        return attendanceRepository.findAll()
            .stream()
            .map(attendanceMapper::toResponse)
            .toList();
    }
}