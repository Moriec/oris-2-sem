package com.vinogradov.service;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserToUserDTOTest {

    private UserToUserDTO userToUserDTO;

    @BeforeEach
    void setUp() {
        userToUserDTO = new UserToUserDTO();
    }

    @Test
    void testВDTO() {
        User user = User.builder()
                .id(1)
                .name("testuser")
                .lastname("lastname")
                .login("login")
                .build();

        UserDTO result = userToUserDTO.toDTO(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("testuser", result.getName());
        assertEquals("lastname", result.getLastname());
        assertEquals("login", result.getLogin());
    }

    @Test
    void testВEntity() {
        UserDTO dto = new UserDTO(1, "testuser", "lastname", "login");

        User result = userToUserDTO.toEntity(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("testuser", result.getName());
        assertEquals("lastname", result.getLastname());
        assertEquals("login", result.getLogin());
    }

    @Test
    void testОбновитьEntity() {
        User user = User.builder()
                .id(1)
                .name("oldname")
                .lastname("oldlastname")
                .login("oldlogin")
                .build();
        UserDTO dto = new UserDTO(1, "newname", "newlastname", "newlogin");

        userToUserDTO.updateEntity(user, dto);

        assertEquals("newname", user.getName());
        assertEquals("newlastname", user.getLastname());
        assertEquals("newlogin", user.getLogin());
    }
}
