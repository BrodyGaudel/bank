package org.mounanga.userservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.userservice.dto.UserEnabledRequest;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.dto.UserRoleRequest;
import org.mounanga.userservice.entity.Profile;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.enums.Gender;
import org.mounanga.userservice.repository.ProfileRepository;
import org.mounanga.userservice.repository.RoleRepository;
import org.mounanga.userservice.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private Role role;
    private UserRoleRequest userRoleRequest;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, profileRepository, passwordEncoder);
        Profile profile = Profile.builder().cin("123456789").build();
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .enabled(true)
                .profile(profile)
                .roles(new ArrayList<>())
                .build();

        userRequest = UserRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .placeOfBirth("City")
                .gender(Gender.M)
                .nationality("Country")
                .cin("123456")
                .email("new@example.com")
                .username("newuser")
                .password("password123")
                .build();

        role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        userRoleRequest = new UserRoleRequest(user.getId(), role.getId());
    }

    @Test
    void testCreateUser() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(profileRepository.existsByCin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResponse = userService.createUser(userRequest);

        assertNotNull(userResponse);
        assertEquals("testuser", userResponse.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResponse = userService.updateUser(1L, userRequest);

        assertNotNull(userResponse);
        assertNotEquals("testuser", userResponse.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddRoleToUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResponse = userService.addRoleToUser(userRoleRequest);

        assertNotNull(userResponse);
        assertNotNull(userResponse.getProfile().getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRemoveRoleFromUser() {
        user.getRoles().add(role);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResponse = userService.removeRoleFromUser(userRoleRequest);

        assertNotNull(userResponse);
        assertNotNull(userResponse.getProfile().getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testEnableOrDisableUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.enableOrDisableUser(new UserEnabledRequest(user.getId()));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserById(1L);

        assertNotNull(userResponse);
        assertEquals("testuser", userResponse.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllUsers() {
        Page<User> userPage = mock(Page.class);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userPage.getContent()).thenReturn(List.of(user));

        List<UserResponse> userResponses = userService.getAllUsers(0, 10);

        assertNotNull(userResponses);
        assertFalse(userResponses.isEmpty());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testSearchUsers() {
        Page<User> userPage = mock(Page.class);
        when(userRepository.findByFirstnameOrLastnameOrCin(anyString(), any(Pageable.class))).thenReturn(userPage);
        when(userPage.getContent()).thenReturn(List.of(user));

        List<UserResponse> userResponses = userService.searchUsers("test", 0, 10);

        assertNotNull(userResponses);
        assertFalse(userResponses.isEmpty());
        verify(userRepository, times(1)).findByFirstnameOrLastnameOrCin(anyString(), any(Pageable.class));
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserByUsername("testuser");

        assertNotNull(userResponse);
        assertEquals("testuser", userResponse.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
}