package com.vinogradov.controller;

import com.vinogradov.model.ChatMessage;
import com.vinogradov.model.User;
import com.vinogradov.repository.ChatMessageRepository;
import com.vinogradov.repository.UserRepository;
import com.vinogradov.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatMessageHandler {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Map<String, Object> sendMessage(Map<String, String> payload, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User author = userRepository.findById(userDetails.getId()).orElseThrow();

        ChatMessage message = ChatMessage.builder()
                .content(payload.get("content"))
                .sentAt(LocalDateTime.now())
                .author(author)
                .build();

        chatMessageRepository.save(message);

        Map<String, Object> response = new HashMap<>();
        response.put("id", message.getId());
        response.put("content", message.getContent());
        response.put("sentAt", message.getSentAt().toString());
        response.put("author", author.getLogin());
        response.put("authorId", author.getId());

        return response;
    }
}
