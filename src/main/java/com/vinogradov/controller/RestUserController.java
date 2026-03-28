package com.vinogradov.controller;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.dto.UserUsernamePasswordMail;
import com.vinogradov.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class RestUserController {

    private final UserService userService;

    @PostMapping("/create-email")
    public void addUser(@RequestBody UserUsernamePasswordMail userDto) {
        userService.createUser(userDto);
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") Integer id) {
        UserDTO userDto = userService.findById(id);
        if(userDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userDto;
    }

    @PostMapping
    public UserDTO create(@RequestBody UserDTO dto) {
        return userService.create(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable("id") Integer id, @RequestBody UserDTO dto) {
        UserDTO updated = userService.update(id, dto);
        if(updated == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        userService.deleteById(id);
    }
}
