package com.vinogradov.controller;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RestUserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserDTO> users() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }
}
