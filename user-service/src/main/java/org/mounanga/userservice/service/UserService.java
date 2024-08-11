package org.mounanga.userservice.service;

import org.mounanga.userservice.dto.UserEnabledRequest;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.dto.UserRoleRequest;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUserById(Long id);
    UserResponse addRoleToUser(UserRoleRequest request);
    UserResponse removeRoleFromUser(UserRoleRequest request);
    UserResponse enableOrDisableUser(UserEnabledRequest request);

    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers(int page, int size);
    List<UserResponse> searchUsers(String keyword, int page, int size);
    UserResponse getUserByUsername(String username);

}
