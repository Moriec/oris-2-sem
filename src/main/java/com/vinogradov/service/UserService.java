package com.vinogradov.service;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.model.User;
import com.vinogradov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public UserDTO create(UserDTO dto) {
        User user = userToUserDTO.toEntity(dto);
        user.setId(null);
        User saved = userRepository.saveAndFlush(user);
        return userToUserDTO.toDTO(saved);
    }

    @Transactional
    public UserDTO update(Integer id, UserDTO dto) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return null;
        }
        userToUserDTO.updateEntity(user, dto);
        User saved = userRepository.save(user);
        return userToUserDTO.toDTO(saved);
    }

    @Transactional
    public UserDTO findByName(String name) {
        return userRepository.findByName(name).map(userToUserDTO::toDTO).orElse(null);
    }
}
