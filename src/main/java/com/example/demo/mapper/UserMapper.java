package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    User updateUser(@MappingTarget User user, UserRegistrationRequestDto requestDto);
}
