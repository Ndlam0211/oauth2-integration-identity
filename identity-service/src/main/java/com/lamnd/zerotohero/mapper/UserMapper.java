package com.lamnd.zerotohero.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lamnd.zerotohero.dto.reponse.UserResponse;
import com.lamnd.zerotohero.dto.request.UserCreationRequest;
import com.lamnd.zerotohero.dto.request.UserUpdateRequest;
import com.lamnd.zerotohero.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true) // ignore roles field when updating user
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toDTO(User user);

    List<UserResponse> toListDTO(List<User> userList);
}
