package com.mounanga.userservice.services.implementations;

import com.mounanga.userservice.entities.Role;
import com.mounanga.userservice.entities.User;
import com.mounanga.userservice.repositories.RoleRepository;
import com.mounanga.userservice.repositories.UserRepository;
import com.mounanga.userservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The ID of the user to retrieve.
     * @return The user associated with the given ID, or null if not found.
     */
    @Override
    public User getUserById(Long id) {
        log.info("In getUserById()");
        User user = userRepository.findById(id).orElse( null);
        if(user==null){
            String message = "user with id: '"+id+"' not found";
            log.warn(message);
            return null;
        }
        log.info("user found");
        return user;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user associated with the given username, or null if not found.
     */
    @Override
    public User getUserByUsername(String username) {
        log.info("In getUserByUsername()");
        User user = userRepository.findByUsername(username);
        if(user==null){
            log.warn("user not found");
            return null;
        }
        log.info("user found");
        return user;
    }

    /**
     * Retrieves a list of users based on pagination parameters.
     *
     * @param size The number of users per page.
     * @param page The page number (0-based) for retrieving users.
     * @return A list of users based on the specified page and size.
     */
    @Override
    public List<User> getAllUsersByPage(int size, int page) {
        log.info("In getAllUsersByPage()");
        Page<User> userPage = userRepository.findAllUsersByPage(PageRequest.of(page, size));
        log.info("user(s) found");
        return userPage.getContent();
    }

    /**
     * Creates a new user.
     *
     * @param user The user object containing the information to create the user.
     * @return The newly created user.
     */
    @Override
    public User createUser(User user) {
        if(user == null){
            log.error("cannot save an user equals to null");
            return null;
        }
        if(Boolean.TRUE.equals(userRepository.checkIfUsernameExists(user.getUsername()))){
            log.error("username already exist");
            return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User userSaved = userRepository.save(user);
        log.info("user saved");
        return userSaved;
    }

    /**
     * Updates an existing user's information.
     *
     * @param id   The ID of the user to update.
     * @param user The updated user object.
     * @return The user object after the update.
     */
    @Override
    public User updateUser(Long id, User user) {
        log.info("In updateUser()");
        User u = userRepository.findById(id).orElse( null);
        if(u == null){
            log.warn("user not found");
            return null;
        }
        if(!u.getUsername().equals(user.getUsername()) && (Boolean.TRUE.equals(userRepository.checkIfUsernameExists(user.getUsername())))){
                log.error("username already exist");
                return null;
        }
        u.setEnabled(user.getEnabled());
        u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        u.setUsername(user.getUsername());

        User userUpdated = userRepository.save(u);
        log.info("user updated");
        return userUpdated;
    }

    /**
     * Adds a role to a user.
     *
     * @param username The username of the user to which the role will be added.
     * @param roleName The name of the role to add.
     * @return The user object after the role has been added.
     */
    @Override
    public User addRoleToUser(String username, String roleName) {
        log.info("In addRoleToUser()");
        User user = getUserByUsername(username);
        Role role = getRoleByName(roleName);
        if(user == null || role == null){
            log.error("user or role not found");
            return null;
        }
        List<Role> userRoles = user.getRoles();
        userRoles.add(role);
        user.setRoles(userRoles);
        User userUpdated = userRepository.save(user);
        log.info("role added to user successfully");
        return userUpdated;
    }

    /**
     * Removes a role from a user.
     *
     * @param username The username of the user from which the role will be removed.
     * @param roleName The name of the role to remove.
     * @return The user object after the role has been removed.
     */
    @Override
    public User removeRoleFromUser(String username, String roleName) {
        log.info("In removeRoleFromUser()");
        Role role = getRoleByName(roleName);
        User user = getUserByUsername(username);
        if(user == null || role == null){
            log.error("user or role not found");
            return null;
        }
        List<Role> userRoles = user.getRoles();
        userRoles.remove(role);
        user.setRoles(userRoles);
        User userUpdated = userRepository.save(user);
        log.info("role removed to user successfully");
        return userUpdated;
    }

    /**
     * Creates a new role.
     *
     * @param role The role object containing the information to create the role.
     * @return The newly created role.
     */
    @Override
    public Role createRole(Role role) {
        log.info("In createRole()");
        if(role == null){
            log.error("cannot save role with value equals to null");
            return null;
        }
        if(Boolean.TRUE.equals(roleRepository.checkIfNameExists(role.getName()))){
            log.warn("role name already exist");
            return null;
        }
        Role roleSaved = roleRepository.save(role);
        log.info("role saved successfully");
        return roleSaved;
    }

    /**
     * get role by name
     *
     * @param name role's name
     * @return role found
     */
    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null){
            log.warn("role not found");
            return null;
        }
        log.info("role found");
        return role;
    }

    /**
     * Deletes a role by its unique identifier.
     *
     * @param roleId The ID of the role to delete.
     */
    @Override
    public void deleteRoleById(Long roleId) {
        log.info("In deleteRoleById()");
        roleRepository.deleteById(roleId);
        log.info("role deleted");
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param userId The ID of the user to delete.
     */
    @Override
    public void deleteUser(Long userId) {
        log.info("In deleteUserById()");
        userRepository.deleteById(userId);
        log.info("user deleted");

    }
}
