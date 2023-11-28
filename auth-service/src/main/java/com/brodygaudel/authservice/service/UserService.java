package com.brodygaudel.authservice.service;

import com.brodygaudel.authservice.entity.Role;
import com.brodygaudel.authservice.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername (String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String roleName);
    User removeRoleToUser(String username, String roleName);
    void deleteUserById(Long id);
    List<User> getAllUsers();
}
