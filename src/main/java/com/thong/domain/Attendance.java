package com.thong.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances",
        uniqueConstraints = {
                // DB-level enforcement: one attendance record per employee per day
                @UniqueConstraint(columnNames = {"employee_id", "date"})
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ManyToOne: many attendance records belong to one employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // Computed on check-out: (checkOutTime - checkInTime) in hours
    private Double totalHours;

    // Business rule: overtime = totalHours - 8, if totalHours > 8, else 0
    private Double overtimeHours;
}