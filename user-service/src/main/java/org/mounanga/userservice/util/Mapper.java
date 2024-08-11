package org.mounanga.userservice.util;

import org.jetbrains.annotations.NotNull;
import org.mounanga.userservice.dto.ProfileResponse;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.entity.Profile;
import org.mounanga.userservice.entity.User;

import java.util.List;

public class Mapper {

    private Mapper() {
        super();
    }

    public static @NotNull UserResponse fromUser(final @NotNull User user) {
        return UserResponse.builder()
                .id(user.getId())
                .enabled(user.getEnabled())
                .email(user.getEmail())
                .username(user.getUsername())
                .createBy(user.getCreateBy())
                .createdDate(user.getCreatedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .lastModifiedDate(user.getLastModifiedDate())
                .profile(fromProfile(user.getProfile(), user.getId()))
                .build();
    }

    public static List<UserResponse> fromListOfUsers(final @NotNull List<User> users) {
        return users.stream()
                .map(Mapper::fromUser)
                .toList();
    }

    public static @NotNull User fromUserRequest(final @NotNull UserRequest userRequest) {
        Profile profile = fromProfile(userRequest);
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .enabled(Boolean.TRUE)
                .passwordNeedToBeModified(Boolean.TRUE)
                .profile(profile)
                .build();
        profile.setUser(user);
        user.setProfile(profile);
        return user;
    }

    private static @NotNull Profile fromProfile(final @NotNull UserRequest userRequest) {
        return Profile.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .nationality(userRequest.getNationality())
                .gender(userRequest.getGender())
                .cin(userRequest.getCin())
                .dateOfBirth(userRequest.getDateOfBirth())
                .placeOfBirth(userRequest.getPlaceOfBirth())
                .build();
    }

    private static ProfileResponse fromProfile(final Profile profile, final Long userId) {
        if (profile == null) {
            return null;
        }
        return ProfileResponse.builder()
                .id(profile.getId())
                .cin(profile.getCin())
                .firstname(profile.getFirstname())
                .lastname(profile.getLastname())
                .placeOfBirth(profile.getPlaceOfBirth())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .nationality(profile.getNationality())
                .createBy(profile.getCreateBy())
                .createdDate(profile.getCreatedDate())
                .lastModifiedBy(profile.getLastModifiedBy())
                .lastModifiedDate(profile.getLastModifiedDate())
                .userId(userId)
                .build();
    }
}

