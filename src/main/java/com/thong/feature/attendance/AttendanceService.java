package com.thong.feature.attendance;

import com.thong.feature.attendance.dto.AttendanceResponse;
import com.thong.feature.attendance.dto.CheckInRequest;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn(Integer employeeId, CheckInRequest request);
    AttendanceResponse checkOut(Integer employeeId);
    List<AttendanceResponse> getMyAttendance(Integer employeeId);
    List<AttendanceResponse> getAllAttendance();
}