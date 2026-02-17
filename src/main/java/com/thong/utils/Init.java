package com.thong.utils;

import com.thong.domain.Role;
import com.thong.domain.User;
import com.thong.feature.role.RoleRepository;
import com.thong.feature.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Init {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRespository;

    @PostConstruct
    public void init() {
        initRoles();
        initAdminUsers();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {

            Role user = new Role();
            user.setName("EMPLOYEE");

            Role admin = new Role();
            admin.setName("ADMIN");

            roleRepository.saveAll(Set.of(user, admin));
        }
    }

    private void initAdminUsers() {

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("EMPLOYEE role not found"));

        createAdminIfNotExists(
                "ThongFazon",
                "thongfazon@gmail.com",
                "admin123",
                adminRole,
                employeeRole
        );

        createAdminIfNotExists(
                "seavthong",
                "seavthong@gmail.com",
                "admin123",
                adminRole,
                employeeRole
        );
    }

    private void createAdminIfNotExists(
            String username,
            String email,
            String rawPassword,
            Role adminRole,
            Role employeeRole
    ) {

        if (userRespository.existsByEmail(email)) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        //  REQUIRED FIELDS (VERY IMPORTANT)
        user.setDob(LocalDate.of(2000, 1, 1));
        user.setGender("MALE"); // or enum if you use enum

        user.setIsDeleted(false);
        user.setIsBlocked(false);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(employeeRole);
        user.setRoles(roles);

        userRespository.save(user);
    }

}
