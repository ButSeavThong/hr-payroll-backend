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
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    private static final double STANDARD_HOURS = 8.0;
    // Add this constant at the top of your service
    private static final ZoneId CAMBODIA_ZONE = ZoneId.of("Asia/Phnom_Penh");
    @Override
    @Transactional
    public AttendanceResponse checkIn(Integer employeeId, CheckInRequest request) {
        //  Use Cambodia time for today's date
        LocalDate date = (request.date() != null)
                ? request.date()
                : LocalDate.now(CAMBODIA_ZONE);

        if (attendanceRepository.findByEmployeeIdAndDate(employeeId, date).isPresent()) {
            throw new IllegalStateException("Already checked in for date: " + date);
        }

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        var attendance = Attendance.builder()
                .employee(employee)
                .date(date)
                .checkInTime(LocalDateTime.now(CAMBODIA_ZONE)) //  Cambodia time
                .build();

        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut(Integer employeeId) {
        //  Use Cambodia date to find today's record
        LocalDate today = LocalDate.now(CAMBODIA_ZONE);

        var attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("No check-in found for today"));

        if (attendance.getCheckOutTime() != null) {
            throw new IllegalStateException("Already checked out today");
        }

        LocalDateTime checkOut = LocalDateTime.now(CAMBODIA_ZONE); //  Cambodia time
        attendance.setCheckOutTime(checkOut);

        Duration duration = Duration.between(attendance.getCheckInTime(), checkOut);
        double totalHours = duration.toMinutes() / 60.0;
        attendance.setTotalHours(totalHours);

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