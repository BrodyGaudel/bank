package org.mounanga.authenticationservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.authenticationservice.dto.*;
import org.mounanga.authenticationservice.entity.Role;
import org.mounanga.authenticationservice.entity.User;
import org.mounanga.authenticationservice.exception.FieldValidationException;
import org.mounanga.authenticationservice.exception.RoleNotFoundException;
import org.mounanga.authenticationservice.exception.UserNotFoundException;
import org.mounanga.authenticationservice.repository.RoleRepository;
import org.mounanga.authenticationservice.repository.UserRepository;
import org.mounanga.authenticationservice.service.UserService;
import org.mounanga.authenticationservice.util.mapper.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserResponseDTO createUser(@NotNull UserRequestDTO dto) {
        log.info("In createUser()");
        validationBeforeCreate(dto.getCin(), dto.getEmail(), dto.getUsername());
        User user = Mappers.fromUserRequestDTO(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User saved with id: {}", savedUser.getId());
        return Mappers.fromUser(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        log.info("In updateUser()");
        User user = findUserById(id);
        validationBeforeUpdate(user,dto);
        updateUserFields(user,dto);
        User savedUser = userRepository.save(user);
        log.info("User with id: {} updated", savedUser.getId());
        return Mappers.fromUser(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDTO updatePassword(String username, @NotNull UpdatePasswordRequestDTO dto) {
        log.info("In updatePassword()");
        User user = findUserByUsername(username);
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        User savedUser = userRepository.save(user);
        log.info("password updated");
        return Mappers.fromUser(savedUser);
    }

    @Override
    public void deleteUserById(String id) {
        log.info("In deleteUserById()");
        userRepository.deleteById(id);
        log.info("User with id {} deleted successfully", id);
    }

    @Transactional
    @Override
    public UserResponseDTO addRoleToUser(@NotNull UserRoleRequestDTO dto) {
        log.info("In addRoleToUser()");
        User user = findUserByUsername(dto.username());
        Role role = findRoleByName(dto.roleName());
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        log.info("role {} added successfully to user {}", dto.roleName(), savedUser.getId());
        return Mappers.fromUser(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDTO removeRoleFromUser(@NotNull UserRoleRequestDTO dto) {
        log.info("In removeRoleFromUser()");
        User user = findUserByUsername(dto.username());
        Role role = findRoleByName(dto.roleName());
        user.getRoles().remove(role);
        User savedUser = userRepository.save(user);
        log.info("role {} removed from user {}", dto.roleName(), savedUser.getId());
        return Mappers.fromUser(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUserStatus(String id) {
        log.info("In updateUserStatus()");
        User user = findUserById(id);
        if(user.isEnabled()){
            user.setEnabled(Boolean.FALSE);
        }else{
            user.setEnabled(Boolean.TRUE);
        }
        User updateUser = userRepository.save(user);
        log.info("Status of user with id {} updated", updateUser.getId());
        return Mappers.fromUser(updateUser);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        log.info("In getUserById()");
        User user = findUserById(id);
        log.info("User with id {} found successfully", id);
        return Mappers.fromUser(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        log.info("In getUserByUsername()");
        User user = findUserByUsername(username);
        log.info("User with name {} found successfully", username);
        return Mappers.fromUser(user);
    }

    @Override
    public PageResponseDTO<UserResponseDTO> getAllUsers(int page, int size) {
        log.info("In getAllUsers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        log.info("{} users found", users.getTotalElements());
        return Mappers.fromPageOfUsers(users);
    }

    @Override
    public PageResponseDTO<UserResponseDTO> searchUsers(String query, int page, int size) {
        log.info("In searchUsers()");
        Pageable pageable = PageRequest.of(page, size);
        String keyword = "%"+query+"%";
        Page<User> users = userRepository.search(keyword, pageable);
        log.info("{} users found.", users.getTotalElements());
        return Mappers.fromPageOfUsers(users);
    }

    private User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(String.format("User with username %s not found", username)) );
    }

    private Role findRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow( () -> new RoleNotFoundException(String.format("Role %s not found", name)) );
    }

    private void validationBeforeCreate(String cin, String email, String username) {
        List<String> messages = new ArrayList<>();
        if(userRepository.existsByUsername(username)) {
            messages.add("Username already exists");
        }
        if(userRepository.existsByEmail(email)) {
            messages.add("Email already exists");
        }
        if(userRepository.existsByCin(cin)) {
            messages.add("Cin already exists");
        }
        if(!messages.isEmpty()){
            throw new FieldValidationException("Invalid username or email or cin", messages);
        }
    }

    private void validationBeforeUpdate(@NotNull User user, @NotNull UserRequestDTO dto) {
        List<String> errors = new ArrayList<>();
        if(!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            errors.add("Username already exists");
        }
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            errors.add("Email already exists");
        }
        if(!user.getCin().equals(dto.getCin()) && userRepository.existsByCin(dto.getCin())) {
            errors.add("Cin already exists");
        }
        if(!errors.isEmpty()){
            throw new FieldValidationException("Invalid username or email or cin", errors);
        }
    }

    private void updateUserFields(@NotNull User user, @NotNull UserRequestDTO dto){
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPlaceOfBirth(dto.getPlaceOfBirth());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setNationality(dto.getNationality());
        user.setGender(dto.getGender());
        user.setCin(dto.getCin());
    }
}
