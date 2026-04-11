package com.vinogradov.controller;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RestUserController.class)
public class RestUserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testНайтиВсех() throws Exception {
        UserDTO userDto = new UserDTO(12, "username", "vinogradov", "logi");
        given(userService.findAll()).willReturn(List.of(userDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()));
    }

    @Test
    void testНайтиПоId() throws Exception {
        UserDTO userDto = new UserDTO(1, "test", "last", "login");
        given(userService.findById(1)).willReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testНайтиПоIdОшибка() throws Exception {
        given(userService.findById(1)).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testУдалить() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                .with(csrf())
                .with(user("user").roles("USER")))
                .andExpect(status().isOk());
        
        verify(userService).deleteById(1);
    }
}
