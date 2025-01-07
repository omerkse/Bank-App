package com.ata.bankapp;

import com.ata.bankapp.model.User;
import com.ata.bankapp.repo.UserRepository;
import com.ata.bankapp.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("testuser@example.com");
    }

    @Test
    public void testRegisterUser() {
        given(passwordEncoder.encode(user.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(user)).willReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals(user.getId(), registeredUser.getId());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindByUsername() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(user);

        User foundUser = userService.findByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    public void testFindByUsername_NotFound() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(null);

        User foundUser = userService.findByUsername(user.getUsername());

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setPassword("newPassword123");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordEncoder.encode(updatedUser.getPassword())).willReturn("encodedNewPassword");
        given(userRepository.save(user)).willReturn(user);

        User result = userService.updateUser(user.getId(), updatedUser);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        verify(passwordEncoder, times(1)).encode(updatedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_NotFound() {
        User updatedUser = new User();
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setPassword("newPassword123");

        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(user.getId(), updatedUser);
        });

        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testGetUser() {
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User result = userService.getUser(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testGetUser_NotFound() {
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.getUser(user.getId());
        });

        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }
}
