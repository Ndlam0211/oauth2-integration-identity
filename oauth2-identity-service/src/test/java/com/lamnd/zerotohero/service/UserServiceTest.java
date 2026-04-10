package com.lamnd.zerotohero.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.lamnd.zerotohero.dto.reponse.UserResponse;
import com.lamnd.zerotohero.dto.request.UserCreationRequest;
import com.lamnd.zerotohero.entity.Role;
import com.lamnd.zerotohero.entity.User;
import com.lamnd.zerotohero.exception.ResourceExistedException;
import com.lamnd.zerotohero.exception.ResourceNotFoundException;
import com.lamnd.zerotohero.repository.RoleRepo;
import com.lamnd.zerotohero.repository.UserRepo;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private RoleRepo roleRepo;

    private UserCreationRequest createUserRequest;
    private UserResponse userResponse;
    private User user;
    private Role role;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(2003, 11, 2);

        createUserRequest = UserCreationRequest.builder()
                .username("user")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("123456789asd")
                .username("user")
                .firstName("Test")
                .lastName("User")
                .dob(dob)
                .build();

        user = User.builder()
                .id("123456789asd")
                .username("user")
                .firstName("Test")
                .lastName("User")
                .dob(dob)
                .build();

        role = Role.builder().name("USER").description("Normal user").build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepo.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepo.save(any())).thenReturn(user);
        Mockito.when(roleRepo.findById(anyString())).thenReturn(java.util.Optional.of(role));

        // WHEN
        var response = userService.createUser(createUserRequest);

        // THEN
        Assertions.assertEquals(userResponse.getId(), response.getId());
        Assertions.assertEquals(userResponse.getUsername(), response.getUsername());
        Assertions.assertEquals(userResponse.getFirstName(), response.getFirstName());
        Assertions.assertEquals(userResponse.getLastName(), response.getLastName());
        Assertions.assertEquals(userResponse.getDob(), response.getDob());
    }

    @Test
    void createUser_existedUser_fail() {
        // GIVEN
        Mockito.when(userRepo.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = Assertions.assertThrows(
                ResourceExistedException.class, () -> userService.createUser(createUserRequest));

        // THEN
        Assertions.assertEquals("User", exception.getResourceName());
        Assertions.assertEquals("username", exception.getFieldName());
        Assertions.assertEquals("user", exception.getFieldValue());
    }

    @Test
    @WithMockUser(username = "user")
    void getMyInfo_validUser_success() {
        Mockito.when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        Assertions.assertEquals(userResponse.getId(), response.getId());
        Assertions.assertEquals(userResponse.getUsername(), response.getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void getMyInfo_userNotFound_fail() {
        Mockito.when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getMyInfo());

        // THEN
        Assertions.assertEquals("User", exception.getResourceName());
        Assertions.assertEquals("username", exception.getFieldName());
        Assertions.assertEquals("user", exception.getFieldValue());
    }
}
