package org.mounanga.userservice.web;

import jakarta.validation.Valid;
import org.mounanga.userservice.dto.UserEnabledRequest;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.dto.UserRoleRequest;
import org.mounanga.userservice.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("/create")
    public UserResponse createUser(@RequestBody @Valid UserRequest request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("/add-role")
    public UserResponse addRoleToUser(@RequestBody @Valid UserRoleRequest request) {
        return userService.addRoleToUser(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("/remove-role")
    public UserResponse removeRoleFromUser(@RequestBody @Valid UserRoleRequest request) {
        return userService.removeRoleFromUser(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("/enable-disable")
    public UserResponse enableOrDisableUser(@RequestBody  @Valid UserEnabledRequest request) {
        return userService.enableOrDisableUser(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/get/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/list")
    public List<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/search")
    public List<UserResponse> searchUsers(@RequestParam String keyword,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return userService.searchUsers(keyword, page, size);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/current")
    public UserResponse getUserByUsername() {
        String username = getCurrentUsername();
        return userService.getUserByUsername(username);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
