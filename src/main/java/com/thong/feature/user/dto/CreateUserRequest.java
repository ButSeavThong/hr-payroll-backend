package com.thong.feature.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest (

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String username,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotNull(message = "Date of birth is required")
        LocalDate dob,

//        @NotBlank(message = "National card ID is required")
//        String nationalCardId,

//        @NotBlank(message = "Phone number is required")
//        String phoneNumber,
//
        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Confirmed password is required")
        String confirmedPassword

) {
}
