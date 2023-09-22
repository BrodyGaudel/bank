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
        User u = userService.getUserById(id);
        return deleteUserPassword(u);
    }

    @GetMapping("/find-user/{username}")
    public User getUserByUsername(@PathVariable(name = "username") String username){
        User u = userService.getUserByUsername(username);
        return deleteUserPassword(u);
    }

    @GetMapping("/list-users/{size}/{page}")
    public List<User> getAllUsersByPage(@PathVariable(name = "size") int size,
                                        @PathVariable(name = "page") int page){
        List<User> users = userService.getAllUsersByPage(size, page);
        return users.stream().map(this::deleteUserPassword).toList();
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user){
        User u =  userService.createUser(user);
        return deleteUserPassword(u);
    }

    @PutMapping("/update-user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        User u = userService.updateUser(id, user);
        return deleteUserPassword(u);
    }

    @PutMapping("/add-role-to-user")
    public User addRoleToUser(@RequestBody FormDTO form){
        User u = userService.addRoleToUser(form.username(), form.roleName());
        return deleteUserPassword(u);
    }

    @PutMapping("/remove-role-to-user")
    public User removeRoleFromUser(@RequestBody FormDTO form){
        User u = userService.removeRoleFromUser(form.username(), form.roleName());
        return deleteUserPassword(u);
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

    private User deleteUserPassword(User user){
        if(user == null){
            return null;
        }
        user.setPassword(null);
        return user;
    }
}
