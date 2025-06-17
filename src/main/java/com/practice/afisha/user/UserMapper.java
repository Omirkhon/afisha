package com.practice.afisha.user;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserShortDto toShortDto(User user) {
        UserShortDto shortDto = new UserShortDto();
        shortDto.setId(user.getId());
        shortDto.setName(user.getName());

        return shortDto;
    }

    List<UserShortDto> toShortDto(List<User> users) {
        return users.stream().map(this::toShortDto).toList();
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    List<UserDto> toDto(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }
}
