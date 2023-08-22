package com.mounanga.userservice;

import com.mounanga.userservice.entities.Role;
import com.mounanga.userservice.entities.User;
import com.mounanga.userservice.repositories.RoleRepository;
import com.mounanga.userservice.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository){
        return args -> {
            List<Role> roleList = roleRepository.findAll();
            List<User> users = userRepository.findAll();

            if(roleList.isEmpty() && users.isEmpty()){
                Role role1 = roleRepository.save( new Role(1L, "USER"));
                Role role2 = roleRepository.save( new Role(2L, "ADMIN"));

                List<Role> rolesAdmin = new ArrayList<>();
                rolesAdmin.add(role1);
                rolesAdmin.add(role2);

                List<Role> rolesUser = new ArrayList<>();
                rolesAdmin.add(role1);

                User user1 = new User(1L, "admin", "admin", true, new Date(), null, rolesAdmin);
                User user2 = new User(2L, "user", "user", true, new Date(), null, rolesUser);
                userRepository.saveAll(List.of(user2, user1));
            }
        };
    }

}
