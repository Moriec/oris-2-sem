package com.vinogradov.controller;

import com.vinogradov.model.Note;
import com.vinogradov.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminNoteController.class)
public class AdminNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteRepository noteRepository;

    @Test
    void testВсеЗаметкиАдмин() throws Exception {
        when(noteRepository.findAll()).thenReturn(List.of(new Note()));

        mockMvc.perform(get("/admin/notes").with(user("admin").authorities(() -> "ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void testУдалитьАдмин() throws Exception {
        when(noteRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/admin/notes/1")
                .with(csrf())
                .with(user("admin").authorities(() -> "ADMIN")))
                .andExpect(status().isOk());
        
        verify(noteRepository).deleteById(1L);
    }
}
