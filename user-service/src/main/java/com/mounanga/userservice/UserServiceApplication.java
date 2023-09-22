package com.mounanga.userservice;

import com.mounanga.userservice.entities.Role;
import com.mounanga.userservice.entities.User;
import com.mounanga.userservice.repositories.RoleRepository;
import com.mounanga.userservice.repositories.UserRepository;
import com.mounanga.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UserServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(UserServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserService userService, UserRepository userRepository){
        return args -> {
            List<Role> roleList = roleRepository.findAll();
            List<User> users = userRepository.findAll();

            if(roleList.isEmpty()){
                log.info("*************** ENREGISTREMENT DES ROLES ***************");
                Role role1 = new Role((long)1, "ADMIN");
                Role role2 = new Role((long)2, "USER");
                roleRepository.saveAll(List.of(role1, role2));
            }
            if(users.isEmpty()){
                log.info("*************** ENREGISTREMENT DES UTILISATEURS PAR DEFAULT ***************");
                List<Role> roles = roleRepository.findAll();
                User user1 = new User((long)1, "admin", "admin", true, roles);
                userService.createUser(user1);

                Role role = roleRepository.findByName("USER");
                List<Role> userRoles = new ArrayList<>();
                userRoles.add(role);
                User user2 = new User((long)2, "user", "user", true, userRoles);
                userService.createUser(user2);
            }
        };
    }


}
