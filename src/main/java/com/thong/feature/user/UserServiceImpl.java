package com.thong.feature.user;

import com.thong.domain.Role;
import com.thong.domain.User;
import com.thong.feature.role.RoleRepository;
import com.thong.feature.user.dto.CreateUserRequest;
import com.thong.feature.user.dto.UpdateProfileRequest;
import com.thong.feature.user.dto.UserProfileResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserSerivce{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserProfileResponse register(CreateUserRequest createUserRequest) {

        // Validate email
        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists");
        }

        // Validate password and confirmed password
        if (!createUserRequest.password().equals(createUserRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }

        // Transfer data from DTO to Domain Model
        User user = userMapper.fromCreateUserRequest(createUserRequest);
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setProfileImage("user-avatar.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set default role
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName("EMPLOYEE").orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,  "Role not found"));
                roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);

     return userMapper.toUserProfileResponse(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,  "User not found"));
        userRepository.delete(user);
    }


    @Override
    public UserProfileResponse getUserProfile(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

//        List<String> roles = user.getRoles()
//                .stream()
//                .map(Role::getName)   // <-- extract role name
//                .toList();

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getDob(),
                user.getIsDeleted(),
                user.getProfileImage()

        );
    }


    @Override
    public UserProfileResponse UpdateProfileById(Integer id, UpdateProfileRequest updateProfileRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        userMapper.toUserPartially(updateProfileRequest, user);
        user = userRepository.save(user);
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getDob(),
                user.getIsDeleted(),
                user.getProfileImage()
        );
    }

    @Override
    public List<UserProfileResponse> getUserProfiles() {
        List<User> users = userRepository.findAll();
        return userMapper.toListOfUserProfileResponse(users);
    }


    @Override
    public void adminCreateNewAdmin(CreateUserRequest createUserRequest) {
        // Validate email
        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists");
        }

        // Validate password and confirmed password
        if (!createUserRequest.password().equals(createUserRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }

        // Transfer data from DTO to Domain Model
        User user = userMapper.fromCreateUserRequest(createUserRequest);
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setProfileImage("user-avatar.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set role admin + user :
        List<Role> roles = new ArrayList<>();
        Role userRole = roleRepository.findByName("USER").orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,  "Role not found"));
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,  "Role not found"));
        roles.add(userRole);
        roles.add(adminRole);
        user.setRoles(roles);

        userRepository.save(user);
    }
}
