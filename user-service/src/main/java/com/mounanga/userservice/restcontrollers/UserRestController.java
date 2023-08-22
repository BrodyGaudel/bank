package com.mounanga.userservice.restcontrollers;

import com.mounanga.userservice.dtos.FormDTO;
import com.mounanga.userservice.entities.Role;
import com.mounanga.userservice.entities.User;
import com.mounanga.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-user/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/find-user/{username}")
    public User getUserByUsername(@PathVariable(name = "username") String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/list-users/{size}/{page}")
    public List<User> getAllUsersByPage(@PathVariable(name = "size") int size,
                                        @PathVariable(name = "page") int page){
        return userService.getAllUsersByPage(size, page);
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping("/update-user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @PutMapping("/add-role-to-user")
    public User addRoleToUser(@RequestBody FormDTO form){
        return userService.addRoleToUser(form.username(), form.roleName());
    }

    @PutMapping("/remove-role-to-user")
    public User removeRoleFromUser(@RequestBody FormDTO form){
        return userService.removeRoleFromUser(form.username(), form.roleName());
    }

    @PostMapping("/create-role")
    public Role createRole(@RequestBody Role role){
        return userService.createRole(role);
    }

    @GetMapping("/get-role/{name}")
    public Role getRoleByName(@PathVariable String name){
        return userService.getRoleByName(name);
    }

    @DeleteMapping("/delete-role/{roleId}")
    public void deleteRoleById(@PathVariable Long roleId){
        userService.deleteRoleById(roleId);
    }

    @DeleteMapping("/delete-user/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }

    /**
     * exception handler
     * @param exception the exception to handler
     * @return the exception's message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
        if(exception == null){
            return new ResponseEntity<>("NULL EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
