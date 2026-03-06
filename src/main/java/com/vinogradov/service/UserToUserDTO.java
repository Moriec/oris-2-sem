package com.vinogradov.service;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserToUserDTO {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getLastname(), user.getLogin());
    }

}
