package org.mounanga.userservice;

import lombok.extern.slf4j.Slf4j;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.repository.RoleRepository;
import org.mounanga.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableFeignClients
@SpringBootApplication
public class UserServiceApplication {

    private static final String SUPER_ADMIN = "SUPER_ADMIN";

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<Role> roles = roleRepository.findAll();
            if(roles.isEmpty()) {
                log.info("No roles found");
                try{
                    Role userRole = new Role(null, "USER");
                    Role adminRole = new Role(null, "ADMIN");
                    Role moderatorRole = new Role(null, "MODERATOR");
                    Role superAdminRole = new Role(null, SUPER_ADMIN);
                    roleRepository.save(userRole);
                    roleRepository.save(adminRole);
                    roleRepository.save(moderatorRole);
                    roleRepository.save(superAdminRole);
                    log.info("Roles 'USER', 'MODERATOR', 'ADMIN' and 'SUPER_ADMIN' created successfully'");
                }catch(Exception e){
                    log.error("Error while creating roles", e);
                }
            }
            User user = userRepository.findByUsername(SUPER_ADMIN).orElse(null);
            if(user == null) {
                log.info("No super admin found");
                try {
                    User superAdmin = new User();
                    superAdmin.setUsername(SUPER_ADMIN);
                    superAdmin.setFirstname(SUPER_ADMIN);
                    superAdmin.setLastname(SUPER_ADMIN);
                    superAdmin.setCin(SUPER_ADMIN);
                    superAdmin.setCreatedBy("SYSTEM");
                    superAdmin.setCreatedDate(LocalDateTime.now());
                    superAdmin.setEmail("superadmin@webank.com");
                    String defaultPassword = UUID.randomUUID().toString();
                    superAdmin.setPassword(passwordEncoder.encode(defaultPassword));
                    superAdmin.setEnabled(true);
                    User savedUser = userRepository.save(superAdmin);
                    log.info("Super admin created successfully with password '{}'", defaultPassword);
                    List<Role> roleList = roleRepository.findAll();
                    savedUser.setRoles(roleList);
                    User updatedUser = userRepository.save(savedUser);
                    log.info("User updated successfully with roles '{}'", updatedUser.getRoles());
                }catch (Exception e) {
                    log.error("Error while creating super admin user", e);
                }
            }
        };
    }
}
