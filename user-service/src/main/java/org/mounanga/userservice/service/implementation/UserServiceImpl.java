package org.mounanga.userservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.userservice.dto.UserEnabledRequest;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.dto.UserRoleRequest;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.exception.FieldError;
import org.mounanga.userservice.exception.FieldValidationException;
import org.mounanga.userservice.exception.RoleNotFoundException;
import org.mounanga.userservice.exception.UserNotFoundException;
import org.mounanga.userservice.repository.ProfileRepository;
import org.mounanga.userservice.repository.RoleRepository;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.service.UserService;
import org.mounanga.userservice.util.Mapper;
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
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserResponse createUser(@NotNull UserRequest request) {
        log.info("In createUser()");
        validateBeforeSaving(request.getUsername(), request.getEmail(), request.getCin());
        User user = Mapper.fromUserRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("user created with id '{}' by '{}' at '{}'.", savedUser.getId(), user.getCreateBy(), user.getCreatedDate());
        return Mapper.fromUser(savedUser);
    }

    @Transactional
    @Override
    public UserResponse updateUser(Long id, @NotNull UserRequest request) {
        log.info("In updateUser()");
        User user = findUserById(id);
        validateBeforeUpdating(user, request.getUsername(), request.getEmail(), request.getCin());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.getProfile().setFirstname(request.getFirstname());
        user.getProfile().setLastname(request.getLastname());
        user.getProfile().setGender(request.getGender());
        user.getProfile().setNationality(request.getNationality());
        user.getProfile().setDateOfBirth(request.getDateOfBirth());
        user.getProfile().setPlaceOfBirth(request.getPlaceOfBirth());
        user.getProfile().setCin(request.getCin());

        User updatedUser = userRepository.save(user);
        log.info("user with id '{}' updated by '{}' at '{}'.", updatedUser.getId(), user.getLastModifiedBy(), user.getLastModifiedDate());
        return Mapper.fromUser(updatedUser);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("In deleteUserById()");
        userRepository.deleteById(id);
        log.info("user with id '{}' deleted", id);
    }

    @Transactional
    @Override
    public UserResponse addRoleToUser(@NotNull UserRoleRequest request) {
        log.info("In addRoleToUser()");
        User user = findUserById(request.userId());
        Role role = findRoleById(request.roleId());
        user.getRoles().add(role);
        User updatedUser = userRepository.save(user);
        log.info("role with name '{}' added to user with id '{}'.", role.getName(), updatedUser.getId());
        return Mapper.fromUser(updatedUser);
    }

    @Transactional
    @Override
    public UserResponse removeRoleFromUser(@NotNull UserRoleRequest request) {
        log.info("In removeRoleFromUser()");
        User user = findUserById(request.userId());
        Role role = findRoleById(request.roleId());
        user.getRoles().remove(role);
        User updatedUser = userRepository.save(user);
        log.info("role with name '{}' removed from User with id '{}'.", role.getName(), updatedUser.getId());
        return Mapper.fromUser(updatedUser);
    }

    @Transactional
    @Override
    public UserResponse enableOrDisableUser(@NotNull UserEnabledRequest request) {
        log.info("In enableOrDisableUser()");
        User user = findUserById(request.userId());
        if(user.isEnabled()){
            user.setEnabled(false);
            log.info("user with id '{}' disabled.", user.getId());
        }else{
            user.setEnabled(true);
            log.info("user with id '{}' enabled.", user.getId());
        }
        User updatedUser = userRepository.save(user);
        return Mapper.fromUser(updatedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("In getUserById()");
        User user = findUserById(id);
        log.info("user with id '{}' found.", user.getId());
        return Mapper.fromUser(user);
    }

    @Override
    public List<UserResponse> getAllUsers(int page, int size) {
        log.info("In getAllUsers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        log.info("users found with page '{}'.", userPage.getNumber());
        return Mapper.fromListOfUsers(userPage.getContent());
    }

    @Override
    public List<UserResponse> searchUsers(String keyword, int page, int size) {
        log.info("In searchUsers()");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByFirstnameOrLastnameOrCin("%"+keyword+"%", pageable);
        log.info("{} users found with page '{}'.", userPage.getNumberOfElements(), userPage.getNumber());
        return Mapper.fromListOfUsers(userPage.getContent());
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        log.info("In getUserByUsername()");
        User user = userRepository.findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(String.format("User with username '%s' not found.", username)));
        log.info("user found.");
        return Mapper.fromUser(user);
    }

    private void validateBeforeSaving(String username, String email, String cin) {
        List<FieldError> errors = new ArrayList<>();
        if(userRepository.existsByUsername(username)){
            errors.add(new FieldError("username", "Username is already in use"));
        }
        if(userRepository.existsByEmail(email)){
            errors.add(new FieldError("email", "Email is already in use"));
        }
        if(profileRepository.existsByCin(cin)){
            errors.add(new FieldError("cin", "Cin is already in use"));
        }
        if(!errors.isEmpty()){
            throw new FieldValidationException(errors, "problem of uniqueness of e-mail, cin or username fields");
        }
    }

    private void validateBeforeUpdating(@NotNull User existingUser, String newUsername, String newEmail, String newCin) {
        List<FieldError> errors = new ArrayList<>();
        if(!existingUser.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)){
                errors.add(new FieldError("username", "Username is already in use"));
        }

        if(!existingUser.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)){
                errors.add(new FieldError("email", "Email is already in use"));
        }

        if(!existingUser.getProfile().getCin().equals(newCin) && profileRepository.existsByCin(newCin)){
                errors.add(new FieldError("cin", "Cin is already in use"));
        }

        if(!errors.isEmpty()){
            throw new FieldValidationException(errors, "problem of uniqueness of e-mail, username and cin fields");
        }
    }


    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(String.format("User with id '%s' not found.", id)));
    }

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow( () -> new RoleNotFoundException(String.format("Role with id '%s' not found.", id)));
    }
}
