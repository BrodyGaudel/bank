package com.mounanga.userservice.services;

import com.mounanga.userservice.entities.Role;
import com.mounanga.userservice.entities.User;

import java.util.List;

/**
 * This interface provides methods for managing user-related operations.
 */
public interface UserService {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The ID of the user to retrieve.
     * @return The user associated with the given ID, or null if not found.
     */
    User getUserById(Long id);

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user associated with the given username, or null if not found.
     */
    User getUserByUsername(String username);

    /**
     * Retrieves a list of users based on pagination parameters.
     *
     * @param size The number of users per page.
     * @param page The page number (0-based) for retrieving users.
     * @return A list of users based on the specified page and size.
     */
    List<User> getAllUsersByPage(int size, int page);

    /**
     * Creates a new user.
     *
     * @param user The user object containing the information to create the user.
     * @return The newly created user.
     */
    User createUser(User user);

    /**
     * Updates an existing user's information.
     *
     * @param id   The ID of the user to update.
     * @param user The updated user object.
     * @return The user object after the update.
     */
    User updateUser(Long id, User user);

    /**
     * Adds a role to a user.
     *
     * @param username  The username of the user to which the role will be added.
     * @param roleName  The name of the role to add.
     * @return The user object after the role has been added.
     */
    User addRoleToUser(String username, String roleName);

    /**
     * Removes a role from a user.
     *
     * @param username  The username of the user from which the role will be removed.
     * @param roleName  The name of the role to remove.
     * @return The user object after the role has been removed.
     */
    User removeRoleFromUser(String username, String roleName);

    /**
     * Creates a new role.
     *
     * @param role The role object containing the information to create the role.
     * @return The newly created role.
     */
    Role createRole(Role role);

    /**
     * get role by name
     * @param name role's name
     * @return role found
     */
    Role getRoleByName(String name);

    /**
     * Deletes a role by its unique identifier.
     *
     * @param roleId The ID of the role to delete.
     */
    void deleteRoleById(Long roleId);

    /**
     * Deletes a user by their unique identifier.
     *
     * @param userId The ID of the user to delete.
     */
    void deleteUser(Long userId);
}

