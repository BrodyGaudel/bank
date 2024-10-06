package org.mounanga.authenticationservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.authenticationservice.dto.PageResponseDTO;
import org.mounanga.authenticationservice.dto.UserRequestDTO;
import org.mounanga.authenticationservice.dto.UserResponseDTO;
import org.mounanga.authenticationservice.dto.UserRoleRequestDTO;
import org.mounanga.authenticationservice.entity.Role;
import org.mounanga.authenticationservice.entity.User;
import org.mounanga.authenticationservice.enums.Gender;
import org.mounanga.authenticationservice.exception.FieldValidationException;
import org.mounanga.authenticationservice.exception.RoleNotFoundException;
import org.mounanga.authenticationservice.exception.UserNotFoundException;
import org.mounanga.authenticationservice.repository.RoleRepository;
import org.mounanga.authenticationservice.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        userRequestDTO.setNationality("American");
        userRequestDTO.setGender(Gender.M);
        userRequestDTO.setCin("123456");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setUsername("johndoe");
        userRequestDTO.setPassword("password");
        userRequestDTO.setFirstname("John");
        userRequestDTO.setLastname("Doe");
        userRequestDTO.setPlaceOfBirth("New York");
    }

    @Test
    void testCreateUserSuccess() {
        User savedUser = new User();
        savedUser.setId("1");

        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByCin(userRequestDTO.getCin())).thenReturn(false);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO responseDTO = userService.createUser(userRequestDTO);
        assertNotNull(responseDTO);
        assertEquals("1", savedUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
        verify(userRepository, times(1)).existsByUsername(userRequestDTO.getUsername());
    }

    @Test
    void testCreateUserFieldAlreadyExists() {
        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(true);
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);
        when(userRepository.existsByCin(userRequestDTO.getCin())).thenReturn(true);
        assertThrows(FieldValidationException.class, () -> userService.createUser(userRequestDTO));
    }

    @Test
    void updateUserSuccess() {
        // Arrange
        String userId = "123";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUser");
        existingUser.setEmail("oldEmail@example.com");
        existingUser.setCin("oldCin");
        existingUser.setGender(Gender.M);
        existingUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        existingUser.setPlaceOfBirth("New York");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(userRequestDTO.getUsername());
        updatedUser.setEmail(userRequestDTO.getEmail());
        updatedUser.setCin(userRequestDTO.getCin());
        updatedUser.setGender(userRequestDTO.getGender());
        updatedUser.setDateOfBirth(userRequestDTO.getDateOfBirth());
        updatedUser.setPlaceOfBirth(userRequestDTO.getPlaceOfBirth());
        updatedUser.setFirstname(userRequestDTO.getFirstname());
        updatedUser.setLastname(userRequestDTO.getLastname());
        updatedUser.setPlaceOfBirth(userRequestDTO.getPlaceOfBirth());
        updatedUser.setNationality(userRequestDTO.getNationality());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByCin(userRequestDTO.getCin())).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // Act
        UserResponseDTO result = userService.updateUser(userId, userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userRequestDTO.getUsername(), result.getUsername());
        assertEquals(userRequestDTO.getEmail(), result.getEmail());
        assertEquals(userRequestDTO.getCin(), result.getCin());
        assertEquals(userRequestDTO.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(userRequestDTO.getPlaceOfBirth(), result.getPlaceOfBirth());
        assertEquals(userRequestDTO.getGender(), result.getGender());
        assertEquals(userRequestDTO.getNationality(), result.getNationality());
        assertEquals(userRequestDTO.getFirstname(), result.getFirstname());
        assertEquals(userRequestDTO.getLastname(), result.getLastname());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUserFieldAlreadyExists() {
        String userId = "123";
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUser");
        existingUser.setEmail("oldEmail@example.com");
        existingUser.setCin("oldCin");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(true);
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);
        when(userRepository.existsByCin(userRequestDTO.getCin())).thenReturn(true);

        assertThrows(FieldValidationException.class, () -> userService.updateUser(userId, userRequestDTO));
    }

    @Test
    void deleteUserById() {
        String userId = "123";
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void addRoleToUserShouldAddRoleWhenValidInput() {
        String username = "user1";
        String roleName = "ROLE_ADMIN";

        User user = new User();
        user.setUsername(username);
        user.setRoles(new ArrayList<>());

        Role role = new Role();
        role.setName(roleName);

        UserRoleRequestDTO dto = new UserRoleRequestDTO(username, roleName);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.addRoleToUser(dto);

        assertNotNull(result);
        assertTrue(user.getRoles().contains(role));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleWhenValidInput() {
        String username = "user1";
        String roleName = "ROLE_ADMIN";

        Role role = new Role();
        role.setName(roleName);

        User user = new User();
        user.setUsername(username);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setRoles(roleList);

        UserRoleRequestDTO dto = new UserRoleRequestDTO(username, roleName);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.removeRoleFromUser(dto);

        assertNotNull(result);
        assertFalse(user.getRoles().contains(role));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addRoleToUserShouldThrowUserNotFoundExceptionWhenUserNotFound() {
        String username = "user1";
        String roleName = "ROLE_ADMIN";

        UserRoleRequestDTO dto = new UserRoleRequestDTO(username, roleName);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addRoleToUser(dto));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void addRoleToUserShouldThrowRoleNotFoundExceptionWhenUserNotFound() {
        String username = "user1";
        String roleName = "ROLE_ADMIN";

        UserRoleRequestDTO dto = new UserRoleRequestDTO(username, roleName);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.addRoleToUser(dto));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByIdShouldReturnUserWhenUserExists() {
        String userId = "123";
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByUsernameShouldReturnUserWhenUserExists() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByUsernameShouldThrowExceptionWhenUserDoesNotExist() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getAllUsersShouldReturnPagedUsers() {
        int page = 0;
        int size = 10;
        List<User> userList = List.of(new User(), new User()); // Simule deux utilisateurs
        Page<User> userPage = new PageImpl<>(userList);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        PageResponseDTO<UserResponseDTO> result = userService.getAllUsers(page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void searchUsersShouldReturnPagedUsersWhenQueryMatches() {
        String query = "test";
        int page = 0;
        int size = 10;
        List<User> userList = List.of(new User(), new User());
        Page<User> userPage = new PageImpl<>(userList);

        when(userRepository.search(anyString(), any(Pageable.class))).thenReturn(userPage);

        PageResponseDTO<UserResponseDTO> result = userService.searchUsers(query, page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).search(anyString(), any(Pageable.class));
    }

    @Test
    void searchUsersShouldReturnEmptyResultWhenNoUsersMatchQuery() {
        String query = "nonexistent";
        int page = 0;
        int size = 10;
        Page<User> emptyPage = Page.empty();

        when(userRepository.search(anyString(), any(Pageable.class))).thenReturn(emptyPage);

        PageResponseDTO<UserResponseDTO> result = userService.searchUsers(query, page, size);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(userRepository, times(1)).search(anyString(), any(Pageable.class));
    }









}