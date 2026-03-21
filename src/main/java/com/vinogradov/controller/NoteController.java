package com.vinogradov.controller;

import com.vinogradov.model.Note;
import com.vinogradov.model.User;
import com.vinogradov.repository.NoteRepository;
import com.vinogradov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoteController {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @GetMapping("/notes")
    public String myNotes(Model model) {
        User user = currentUser();
        List<Note> notes = noteRepository.findByAuthor(user);
        model.addAttribute("notes", notes);
        return "notes";
    }

    @GetMapping("/notes/public")
    public String publicNotes(Model model) {
        List<Note> notes = noteRepository.findByIsPublicTrue();
        model.addAttribute("notes", notes);
        return "public_notes";
    }

    @GetMapping("/notes/create")
    public String createForm(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("action", "/notes/create");
        return "note_form";
    }

    @PostMapping("/notes/create")
    public String create(@RequestParam("title") String title,
                         @RequestParam("content") String content,
                         @RequestParam(name = "isPublic", required = false) boolean isPublic) {
        User user = currentUser();
        Note note = Note.builder()
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .author(user)
                .build();
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/notes/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = currentUser();
        if (!note.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("note", note);
        model.addAttribute("action", "/notes/" + id + "/edit");
        return "note_form";
    }

    @PostMapping("/notes/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       @RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @RequestParam(name = "isPublic", required = false) boolean isPublic) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = currentUser();
        if (!note.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        note.setTitle(title);
        note.setContent(content);
        note.setPublic(isPublic);
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @PostMapping("/notes/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = currentUser();
        if (!note.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        noteRepository.deleteById(id);
        return "redirect:/notes";
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByName(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }
}
