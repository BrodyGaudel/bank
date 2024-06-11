package org.mounanga.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.repository.RoleRepository;
import org.mounanga.userservice.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);

        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setUsername("testuser");
        userRequest.setPassword("password123");
        userRequest.setFirstname("John");
        userRequest.setLastname("Doe");
        userRequest.setCin("12345678");

        user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setCin("12345678");
        user.setEnabled(true);
        user.setRoles(new ArrayList<>());

        role = new Role();
        role.setName("USER");
    }

    @Test
    void testCreateUser() {
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByCin(userRequest.getCin())).thenReturn(false);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(userRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserRequest updateRequest = new UserRequest();
        updateRequest.setFirstname("Jane");
        updateRequest.setLastname("Doe");
        updateRequest.setCin("87654321");
        updateRequest.setEmail("newtest@example.com");
        updateRequest.setUsername("newtestuser");
        updateRequest.setPassword("newpassword123");

        UserResponse response = userService.updateUser("1", updateRequest);

        assertNotNull(response);
        assertEquals("Jane", response.getFirstname());
        assertEquals("Doe", response.getLastname());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteById("1");

        userService.deleteUserById("1");

        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById("1");

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserByEmail("test@example.com");

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserByUsername("testuser");

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testAddRoleToUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.addRoleToUser("USER", "testuser");

        assertNotNull(response);
        assertTrue(response.getRoles().contains("USER"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRemoveRoleFromUser() {
        user.getRoles().add(role);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.removeRoleFromUser("USER", "testuser");

        assertNotNull(response);
        assertFalse(response.getRoles().contains("USER"));
        verify(userRepository, times(1)).save(any(User.class));
    }
}