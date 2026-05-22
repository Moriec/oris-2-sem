package com.vinogradov.controller;

import com.vinogradov.model.ChatMessage;
import com.vinogradov.model.User;
import com.vinogradov.repository.ChatMessageRepository;
import com.vinogradov.repository.UserRepository;
import com.vinogradov.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String chat(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("currentUserId", userDetails.getId());
        return "chat";
    }

    @GetMapping("/public")
    public String publicChat(Model model) {
        List<ChatMessage> messages = chatMessageRepository.findTop50ByOrderBySentAtDesc();
        Collections.reverse(messages);
        model.addAttribute("messages", messages);
        return "public_chat";
    }

    @GetMapping("/my")
    public String myMessages(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User author = userRepository.findById(userDetails.getId()).orElseThrow();
        List<ChatMessage> messages = chatMessageRepository.findByAuthor(author);
        Collections.reverse(messages);
        model.addAttribute("messages", messages);
        return "my_messages";
    }

    @PostMapping("/{id}/delete")
    public String deleteMessage(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        ChatMessage message = chatMessageRepository.findById(id)
                .orElse(null);

        if (message == null) {
            return "redirect:/chat/my?error=not_found";
        }

        if (!message.getAuthor().getId().equals(userDetails.getId())) {
            return "redirect:/chat/my?error=forbidden";
        }

        chatMessageRepository.deleteById(id);
        return "redirect:/chat/my";
    }
}
