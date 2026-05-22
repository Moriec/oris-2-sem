package com.vinogradov.controller;

import com.vinogradov.model.ChatMessage;
import com.vinogradov.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
public class AdminChatController {
    private final ChatMessageRepository chatMessageRepository;

    @GetMapping
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        return ResponseEntity.ok(chatMessageRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        if (!chatMessageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chatMessageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
