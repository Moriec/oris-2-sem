package com.vinogradov.service;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.model.User;
import com.vinogradov.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserToUserDTO userToUserDTO;

    @Transactional
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userToUserDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO findById(Integer id) {
        return userRepository.findById(id).map(userToUserDTO::toDTO).orElse(null);
    }

    @Transactional
    public UserDTO save(User user) {
        User saved = userRepository.save(user);
        return userToUserDTO.toDTO(saved);
    }

    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
