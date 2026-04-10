package com.lamnd.zerotohero.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.lamnd.zerotohero.annotation.DobConstraint;
import com.lamnd.zerotohero.config.AppConstants;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank
    @Size(min = 3, message = "username must be at least 3 characters")
    String username;

    @Size(min = 8, message = "password must be at least 8 characters")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(
            minAge = AppConstants.MINAGE,
            message = "User must be at least " + AppConstants.MINAGE + " years old")
    LocalDate dob;
}
