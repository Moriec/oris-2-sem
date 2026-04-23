package com.vinogradov.service;

import com.vinogradov.annotation.Benchmark;
import com.vinogradov.annotation.Metric;
import com.vinogradov.dto.UserDTO;
import com.vinogradov.dto.UserUsernamePasswordMail;
import com.vinogradov.model.Role;
import com.vinogradov.model.User;
import com.vinogradov.properties.MailProperties;
import com.vinogradov.repository.RoleRepository;
import com.vinogradov.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserToUserDTO userToUserDTO;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final MailProperties mailProperties;
    private final JavaMailSender mailSender;

    @Deprecated
    @Transactional
    public void createRoles(){
        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);
    }

    @Metric
    @Benchmark
    @Transactional
    public void registerNewUser(String username, String rawPassword) {
        List<Role> roles = roleRepository.findAll();

        if (userRepository.findByName(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь олреди екзист");
        }

        Role userRole = roleRepository.findByName("USER").orElseThrow(
                () -> {throw new IllegalArgumentException("нету юзер роли в таблице");});

        User user = User.builder()
                .name(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
    }

    public void createUser(UserUsernamePasswordMail userDto) {
        String verificationCode = UUID.randomUUID().toString();
        User user = User.builder()
                .name(userDto.username())
                .password(passwordEncoder.encode(userDto.password()))
                .mail(userDto.mail())
                .verificationCode(verificationCode)
                .build();
        userRepository.save(user);

        sendVerificationMail(userDto, verificationCode);
    }

    private void sendVerificationMail(UserUsernamePasswordMail userDto, String verificationCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        String content = mailProperties.content();
        try {
            mimeMessageHelper.setFrom(mailProperties.from(), mailProperties.sender());
            mimeMessageHelper.setTo(userDto.mail());
            mimeMessageHelper.setSubject(mailProperties.subject());

            content = content.replace("$name", userDto.username());
            content = content.replace("$url", mailProperties.baseUrl() +
                    "/verification?code=" + verificationCode);

            mimeMessageHelper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Metric
    @Benchmark
    @Transactional
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userToUserDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Benchmark
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

    @Transactional
    public void verifyUser(String code) {
        userRepository.findByVerificationCode(code)
                .ifPresentOrElse(user -> {
                            user.setVerified(true);
                            user.setVerificationCode(null);
                        },
                        () -> {
                            throw new IllegalArgumentException("verification код не существует");
                        });
    }
}
