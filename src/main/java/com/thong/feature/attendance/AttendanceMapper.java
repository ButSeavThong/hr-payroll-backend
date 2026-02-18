package com.thong.feature.attendance;

import com.thong.domain.Attendance;
import com.thong.feature.attendance.dto.AttendanceResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(Attendance attendance) {
        String name = attendance.getEmployee().getFirstName()
            + " " + attendance.getEmployee().getLastName();
        return new AttendanceResponse(
            attendance.getId(),
            attendance.getEmployee().getId(),
            name,
            attendance.getDate(),
            attendance.getCheckInTime(),
            attendance.getCheckOutTime(),
            attendance.getTotalHours(),
            attendance.getOvertimeHours()
        );
    }
}