package com.lamnd.zerotohero.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.lamnd.zerotohero.annotation.DobConstraint;
import com.lamnd.zerotohero.config.AppConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;

    @DobConstraint(
            minAge = AppConstants.MINAGE,
            message = "User must be at least " + AppConstants.MINAGE + " years old")
    private LocalDate dob;

    private List<String> roles;
}
