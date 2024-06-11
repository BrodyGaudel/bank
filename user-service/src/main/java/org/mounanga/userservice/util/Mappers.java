package org.mounanga.userservice.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.userservice.dto.PageModel;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mappers {

    private Mappers() {
        super();
    }

    public static UserResponse fromUser(final @NotNull User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .enabled(user.getEnabled())
                .cin(user.getCin())
                .email(user.getEmail())
                .createBy(user.getCreatedBy())
                .lastModifiedBy(user.getLastModifiedBy())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .roles(getRolesNames(user.getRoles()))
                .build();
    }

    public static @NotNull User fromUserRequest(final @NotNull UserRequest userRequest, final @NotNull String encodedPassword) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setCin(userRequest.getCin());
        user.setPassword(encodedPassword);
        return user;
    }

    public static List<UserResponse> fromUserList(final @NotNull List<User> userList) {
        return userList.stream().map(Mappers::fromUser).toList();
    }

    public static @NotNull PageModel<UserResponse> fromPageOfUser(final @NotNull Page<User> userPage, final int page) {
        PageModel<UserResponse> pageModel = new PageModel<>();
        pageModel.setContent(fromUserList(userPage.getContent()));
        pageModel.setTotalElements(userPage.getTotalElements());
        pageModel.setTotalPages(userPage.getTotalPages());
        pageModel.setNumber(userPage.getNumber());
        pageModel.setSize(userPage.getSize());
        pageModel.setNumberOfElements(userPage.getNumberOfElements());
        pageModel.setHasNext(userPage.hasNext());
        pageModel.setHasPrevious(userPage.hasPrevious());
        pageModel.setHasContent(userPage.hasContent());
        pageModel.setFirst(userPage.isFirst());
        pageModel.setLast(userPage.isLast());
        pageModel.setPage(page);
        return pageModel;
    }

    @Contract("_ -> new")
    private static @NotNull Set<String> getRolesNames(final List<Role> roles) {
        if(roles == null || roles.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roles.stream().map(Role::getName).toList());
    }
}
