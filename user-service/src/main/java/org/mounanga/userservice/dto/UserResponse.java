package org.mounanga.userservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserResponse{
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String cin;
    private boolean enabled;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createBy;
    private String lastModifiedBy;
    private Set<String> roles;
}
