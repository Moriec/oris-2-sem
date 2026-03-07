package com.vinogradov.service;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserToUserDTO {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getLastname(), user.getLogin());
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setLastname(dto.getLastname());
        user.setLogin(dto.getLogin());
        return user;
    }

    public void updateEntity(User user, UserDTO dto) {
        user.setName(dto.getName());
        user.setLastname(dto.getLastname());
        user.setLogin(dto.getLogin());
    }
}
