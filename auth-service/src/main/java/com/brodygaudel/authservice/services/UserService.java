package com.brodygaudel.authservice.services;

import com.brodygaudel.authservice.entities.Role;
import com.brodygaudel.authservice.entities.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername (String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
    User removeRoleToUser(String username, String rolename);
    void deleteUserById(Long id);
    List<User> getAllUsers();
}
