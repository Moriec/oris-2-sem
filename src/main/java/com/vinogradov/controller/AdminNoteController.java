package com.vinogradov.controller;

import com.vinogradov.model.Note;
import com.vinogradov.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/notes")
@RequiredArgsConstructor
public class AdminNoteController {
    private final NoteRepository noteRepository;

    @GetMapping
    public List<Note> all() {
        return noteRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        noteRepository.deleteById(id);
    }
}
