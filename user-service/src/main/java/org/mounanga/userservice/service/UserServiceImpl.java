package org.mounanga.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.mounanga.userservice.dto.PageModel;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.exception.RoleNotFoundException;
import org.mounanga.userservice.exception.UserNotFoundException;
import org.mounanga.userservice.repository.RoleRepository;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.util.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        validationBeforeSave(request);
        User user = Mappers.fromUserRequest(request, passwordEncoder.encode(request.getPassword()));
        user.setRoles(findUserRoles());
        User savedUser = userRepository.save(user);
        log.info("User saved successfully with id '{}', by '{}', at '{}'", savedUser.getId(), savedUser.getCreatedBy(), savedUser.getCreatedDate());
        return Mappers.fromUser(savedUser);
    }

    @Transactional
    @Override
    public UserResponse updateUser(String id, UserRequest request) {
        log.info("In updateUser()");
        User user = findUserById(id);
        validationBeforeUpdate(user, request);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setCin(request.getCin());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id '{}', by '{}', at '{}'", updatedUser.getId(), updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate());
        return Mappers.fromUser(updatedUser);
    }

    @Override
    public void deleteUserById(String id) {
        log.info("In deleteUserById()");
        userRepository.deleteById(id);
        log.info("user deleted successfully");
    }

    @Override
    public PageModel<UserResponse> getAllUsers(int page, int size) {
        log.info("In getAllUsers()");
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        log.info("{} user(s) found", users.getTotalElements());
        return Mappers.fromPageOfUser(users, page);
    }

    @Override
    public PageModel<UserResponse> searchUsers(String keyword, int page, int size) {
        log.info("In searchUsers()");
        Page<User> users = userRepository.searchAllByFirstNameOrLastNameOrCin("%" + keyword + "%", PageRequest.of(page, size));
        log.info("{} user(s) found for keyword '{}'", users.getTotalElements(), keyword);
        return Mappers.fromPageOfUser(users, page);
    }

    @Override
    public UserResponse getUserById(String id) {
        log.info("In getUserById()");
        User user = findUserById(id);
        log.info("User found with id {}", user.getId());
        return Mappers.fromUser(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("In getUserByEmail()");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        log.info("User found with email {}", user.getEmail());
        return Mappers.fromUser(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        log.info("In getUserByUsername()");
        User user = findUserByUsername(username);
        log.info("User found: {}", user.getFullName());
        return Mappers.fromUser(user);
    }

    @Transactional
    @Override
    public UserResponse addRoleToUser(String roleName, String username) {
        log.info("In addRoleToUser()");
        User user = findUserByUsername(username);
        Role role = findRoleByName(roleName);
        user.getRoles().add(role);
        User userUpdated = userRepository.save(user);
        log.info("role '{}' added to user '{}'.", roleName, userUpdated.getUsername());
        return Mappers.fromUser(userUpdated);
    }

    @Transactional
    @Override
    public UserResponse removeRoleFromUser(String roleName, String username) {
        log.info("In removeRoleFromUser()");
        User user = findUserByUsername(username);
        Role role = findRoleByName(roleName);
        user.getRoles().remove(role);
        User userUpdated = userRepository.save(user);
        log.info("role '{}' removed from user '{}'.", roleName, userUpdated.getUsername());
        return Mappers.fromUser(userUpdated);
    }

    private User findUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    private Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException("role not found"));
    }

    private @NotNull @Unmodifiable List<Role> findUserRoles() {
        Role role = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(new Role(null, "USER")));
        return Collections.singletonList(role);
    }


    private void validationBeforeSave(@NotNull UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already in use.");
        }
        if (userRepository.existsByCin(request.getCin())) {
            throw new IllegalArgumentException("CIN is already in use.");
        }
    }

    private void validationBeforeUpdate(@NotNull User user, @NotNull UserRequest request) {
        if(!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email is already in use.");
        }
        if(!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username is already in use.");
        }
        if(!user.getCin().equals(request.getCin()) && userRepository.existsByCin(request.getCin())){
            throw new IllegalArgumentException("CIN is already in use.");
        }
    }
}
