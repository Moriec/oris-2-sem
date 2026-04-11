package com.vinogradov.service;

import com.vinogradov.dto.UserDTO;
import com.vinogradov.dto.UserUsernamePasswordMail;
import com.vinogradov.model.Role;
import com.vinogradov.model.User;
import com.vinogradov.properties.MailProperties;
import com.vinogradov.repository.RoleRepository;
import com.vinogradov.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToUserDTO userToUserDTO;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailProperties mailProperties;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("testuser")
                .password("encodedPassword")
                .build();
        userDTO = new UserDTO(1, "testuser", "lastname", "login");
    }

    @Test
    void testНайтиВсех() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userToUserDTO.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getName(), result.get(0).getName());
    }

    @Test
    void testНайтиПоId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userToUserDTO.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(1);

        assertNotNull(result);
        assertEquals(userDTO.getName(), result.getName());
    }

    @Test
    void testНайтиПоIdПусто() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO result = userService.findById(1);

        assertNull(result);
    }

    @Test
    void testРегистрация() {
        when(userRepository.findByName("newuser")).thenReturn(Optional.empty());
        Role userRole = new Role();
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        userService.registerNewUser("newuser", "password");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testРегистрацияБезРоли() {
        when(userRepository.findByName("newuser")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
            userService.registerNewUser("newuser", "password")
        );
    }

    @Test
    void testРегистрацияДубль() {
        when(userRepository.findByName("testuser")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> 
            userService.registerNewUser("testuser", "password")
        );
    }

    @Test
    void testВерификация() {
        user.setVerificationCode("valid_code");
        when(userRepository.findByVerificationCode("valid_code")).thenReturn(Optional.of(user));

        userService.verifyUser("valid_code");

        assertTrue(user.getVerified());
        assertNull(user.getVerificationCode());
    }

    @Test
    void testВерификацияОшибка() {
        when(userRepository.findByVerificationCode("invalid_code")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
            userService.verifyUser("invalid_code")
        );
    }

    @Test
    void testУдалитьПоId() {
        userService.deleteById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testСоздать() {
        when(userToUserDTO.toEntity(userDTO)).thenReturn(user);
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        when(userToUserDTO.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.create(userDTO);

        assertNotNull(result);
        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    void testОбновить() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userToUserDTO.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.update(1, userDTO);

        assertNotNull(result);
        verify(userToUserDTO).updateEntity(eq(user), eq(userDTO));
    }

    @Test
    void testОбновитьПусто() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO result = userService.update(1, userDTO);

        assertNull(result);
    }

    @Test
    void testНайтиПоИмени() {
        when(userRepository.findByName("testuser")).thenReturn(Optional.of(user));
        when(userToUserDTO.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.findByName("testuser");

        assertNotNull(result);
        assertEquals(userDTO.getName(), result.getName());
    }

    @Test
    void testНайтиПоИмениПусто() {
        when(userRepository.findByName("unknown")).thenReturn(Optional.empty());

        UserDTO result = userService.findByName("unknown");

        assertNull(result);
    }

    @Test
    void testСоздатьРоли() {
        userService.createRoles();
        verify(roleRepository, times(2)).save(any(Role.class));
    }

    @Test
    void testСоздатьСПочтой() {
        UserUsernamePasswordMail dto = new UserUsernamePasswordMail("user", "pass", "mail@mail.com");
        MimeMessage mockMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);
        when(mailProperties.content()).thenReturn("Hello $name, verify: $url");
        when(mailProperties.from()).thenReturn("from@mail.com");
        when(mailProperties.sender()).thenReturn("Sender");
        when(mailProperties.subject()).thenReturn("Subject");
        when(mailProperties.baseUrl()).thenReturn("http://localhost");

        userService.createUser(dto);

        verify(userRepository).save(any(User.class));
        verify(mailSender).send(any(MimeMessage.class));
    }
}
