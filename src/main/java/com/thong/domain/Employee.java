package com.thong.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* ===== Relationship with User ===== */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /* ===== Employee Info ===== */
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String department;

    private String position;

    @Column(nullable = false)
    private BigDecimal baseSalary;

    private LocalDate hireDate;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    /* ===== Relationships ===== */
    @OneToMany(mappedBy = "employee")
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "employee")
    private List<Payroll> payrolls;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
