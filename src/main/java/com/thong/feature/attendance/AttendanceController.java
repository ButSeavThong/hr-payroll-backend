package com.thong.feature.attendance;

import com.thong.feature.attendance.dto.AttendanceResponse;
import com.thong.feature.attendance.dto.CheckInRequest;
import com.thong.feature.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    // Resolve JWT username → employee ID (used on every secured endpoint)
    private Integer resolveEmployeeId(UserDetails userDetails) {
        return employeeRepository.findByUserUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("No employee profile found"))
            .getId();
    }

    // POST /api/attendance/check-in
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AttendanceResponse> checkIn(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody(required = false) CheckInRequest request) {
        Integer empId = resolveEmployeeId(userDetails);
        CheckInRequest req = (request != null) ? request : new CheckInRequest(null);
        return ResponseEntity.ok(attendanceService.checkIn(empId, req));
    }

    // POST /api/attendance/check-out
    @PostMapping("/check-out")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AttendanceResponse> checkOut(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(attendanceService.checkOut(resolveEmployeeId(userDetails)));
    }

    // GET /api/attendance/my — employee sees only their own records
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(resolveEmployeeId(userDetails)));
    }

    // GET /api/attendance — admin sees all records
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
}