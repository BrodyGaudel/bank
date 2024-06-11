package org.mounanga.userservice.service;

import org.mounanga.userservice.dto.PageModel;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(String id, UserRequest request);
    void deleteUserById(String id);
    PageModel<UserResponse> getAllUsers(int page, int size);
    PageModel<UserResponse> searchUsers(String keyword, int page, int size);
    UserResponse getUserById(String id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByUsername(String username);
    UserResponse addRoleToUser(String roleName, String username);
    UserResponse removeRoleFromUser(String roleName, String username);
}
