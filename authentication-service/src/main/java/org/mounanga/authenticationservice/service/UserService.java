package org.mounanga.authenticationservice.service;

import org.mounanga.authenticationservice.dto.*;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO updateUser(String id, UserRequestDTO dto);
    UserResponseDTO updatePassword(String username, UpdatePasswordRequestDTO dto);
    void deleteUserById(String id);
    UserResponseDTO addRoleToUser(UserRoleRequestDTO dto);
    UserResponseDTO removeRoleFromUser(UserRoleRequestDTO dto);
    UserResponseDTO updateUserStatus(String id);

    UserResponseDTO getUserById(String id);
    UserResponseDTO getUserByUsername(String username);
    PageResponseDTO<UserResponseDTO> getAllUsers(int page, int size);
    PageResponseDTO<UserResponseDTO> searchUsers(String query, int page, int size);

}
