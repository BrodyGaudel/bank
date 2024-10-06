package org.mounanga.authenticationservice.util.mapper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.authenticationservice.dto.*;
import org.mounanga.authenticationservice.entity.Role;
import org.mounanga.authenticationservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class Mappers {

    private Mappers() {
        super();
    }

    public static @NotNull UserResponseDTO fromUser(final @NotNull User user) {
        final UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setPlaceOfBirth(user.getPlaceOfBirth());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setNationality(user.getNationality());
        dto.setGender(user.getGender());
        dto.setCin(user.getCin());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setEnabled(user.isEnabled());
        dto.setPasswordNeedToBeModified(user.isPasswordNeedToBeModified());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreateBy(user.getCreateBy());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setLastModifiedBy(user.getLastModifiedBy());
        dto.setLastModifiedDate(user.getLastModifiedDate());
        dto.setRoles(rolesToList(user.getRoles()));
        return dto;
    }

    public static List<UserResponseDTO> fromListOfUsers(final List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }
        return users.stream().map(Mappers::fromUser).toList();
    }

    public static @NotNull PageResponseDTO<UserResponseDTO> fromPageOfUsers(final @NotNull Page<User> userPage) {
        final PageResponseDTO<UserResponseDTO> dto = new PageResponseDTO<>();
        dto.setTotalElements(userPage.getTotalElements());
        dto.setTotalPages(userPage.getTotalPages());
        dto.setNumber(userPage.getNumber());
        dto.setSize(userPage.getSize());
        dto.setNumberOfElements(userPage.getNumberOfElements());
        dto.setHasContent(userPage.hasContent());
        dto.setHasPrevious(userPage.hasPrevious());
        dto.setHasNext(userPage.hasNext());
        dto.setLast(userPage.isLast());
        dto.setFirst(userPage.isFirst());
        dto.setContent(fromListOfUsers(userPage.getContent()));
        return dto;
    }

    public static @NotNull User fromUserRequestDTO(final @NotNull UserRequestDTO dto) {
        final User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPlaceOfBirth(dto.getPlaceOfBirth());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setNationality(dto.getNationality());
        user.setCin(dto.getCin());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setPasswordNeedToBeModified(Boolean.TRUE);
        user.setEnabled(Boolean.FALSE);
        return user;
    }

    public static @NotNull Role fromRoleRequestDTO(final @NotNull RoleRequestDTO dto) {
        final Role role = new Role();
        role.setName(dto.name());
        role.setDescription(dto.description());
        return role;
    }

    @Contract("_ -> new")
    public static @NotNull RoleResponseDTO fromRole(final @NotNull Role role) {
        return new RoleResponseDTO(role.getId(), role.getName(), role.getDescription());
    }

    public static List<RoleResponseDTO> fromListOfRoles(final List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        List<RoleResponseDTO> dtoList = new ArrayList<>();
        for(Role role : roles) {
            dtoList.add(fromRole(role));
        }
        return dtoList;
    }

    public static @NotNull PageResponseDTO<RoleResponseDTO> fromPageOfRoles(final @NotNull Page<Role> rolePage) {
        final PageResponseDTO<RoleResponseDTO> dto = new PageResponseDTO<>();
        dto.setNumber(rolePage.getNumber());
        dto.setSize(rolePage.getSize());
        dto.setHasPrevious(rolePage.hasPrevious());
        dto.setHasNext(rolePage.hasNext());
        dto.setLast(rolePage.isLast());
        dto.setFirst(rolePage.isFirst());
        dto.setContent(fromListOfRoles(rolePage.getContent()));
        dto.setTotalElements(rolePage.getTotalElements());
        dto.setTotalPages(rolePage.getTotalPages());
        dto.setNumberOfElements(rolePage.getNumberOfElements());
        dto.setHasContent(rolePage.hasContent());
        return dto;
    }

    private static List<String> rolesToList(final List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream().map(Role::getName).toList();
    }
}
