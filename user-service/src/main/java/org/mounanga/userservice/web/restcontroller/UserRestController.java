package org.mounanga.userservice.web.restcontroller;

import jakarta.validation.Valid;
import org.mounanga.userservice.dto.PageModel;
import org.mounanga.userservice.dto.UserRequest;
import org.mounanga.userservice.dto.UserResponse;
import org.mounanga.userservice.dto.UserRoleRequest;
import org.mounanga.userservice.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("/create")
    public UserResponse createUser(@RequestBody UserRequest request){
        return userService.createUser(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable String id,@RequestBody @Valid UserRequest request){
        return userService.updateUser(id, request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/add-role")
    public UserResponse addRoleToUser(@RequestBody @Valid UserRoleRequest request){
        return userService.addRoleToUser(request.roleName(), request.username());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/remove-role")
    public UserResponse removeRoleFromUser(@RequestBody @Valid UserRoleRequest request){
        return userService.removeRoleFromUser(request.roleName(), request.username());
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable String id){
        userService.deleteUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @GetMapping("/all")
    public PageModel<UserResponse> getAllUsers(@RequestParam(name= "page", defaultValue = "0") int page,
                                               @RequestParam(name= "size", defaultValue = "10") int size){
        return userService.getAllUsers(page, size);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @GetMapping("/search")
    public PageModel<UserResponse> searchUsers(@RequestParam(name = "keyword", defaultValue = " ") String keyword,
                                               @RequestParam(name= "page", defaultValue = "0") int page,
                                               @RequestParam(name= "size", defaultValue = "10") int size){
        return userService.searchUsers(keyword, page, size);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @GetMapping("/get/{id}")
    public UserResponse getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @GetMapping("/find/{email}")
    public UserResponse getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @GetMapping("/profile")
    public UserResponse getUserByUsername(){
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
