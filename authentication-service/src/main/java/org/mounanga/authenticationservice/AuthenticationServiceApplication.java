package org.mounanga.authenticationservice;

import lombok.extern.slf4j.Slf4j;
import org.mounanga.authenticationservice.entity.Role;
import org.mounanga.authenticationservice.entity.User;
import org.mounanga.authenticationservice.enums.Gender;
import org.mounanga.authenticationservice.repository.RoleRepository;
import org.mounanga.authenticationservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@EnableFeignClients
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!roleRepository.existsBy()) {
                log.info("No roles found");
                try {
                    Role userRole = Role.builder().name("USER").description("USER").build();
                    Role adminRole = Role.builder().name("ADMIN").description("ADMIN").build();
                    Role superAdminRole = Role.builder().name("SUPER_ADMIN").description("SUPER_ADMIN").build();
                    roleRepository.save(userRole);
                    roleRepository.save(adminRole);
                    roleRepository.save(superAdminRole);
                    log.info("Roles 'USER', 'MODERATOR', 'ADMIN' and 'SUPER_ADMIN' created successfully'");
                } catch (Exception e) {
                    log.error("Error while creating roles", e);
                }
            }
            User user = userRepository.findByUsername("ADMINISTRATOR").orElse(null);
            if (user == null) {
                log.info("No super admin found");
                try {
                    User superAdmin = new User();
                    superAdmin.setCin("Administrator CIN");
                    superAdmin.setFirstname("Administrator's First Name");
                    superAdmin.setLastname("Administrator's Last Name");
                    superAdmin.setNationality("Administrator Nationality");
                    superAdmin.setDateOfBirth(LocalDate.of(1994, 1, 22));
                    superAdmin.setPlaceOfBirth("Administrator's Birthday");
                    superAdmin.setGender(Gender.M);
                    superAdmin.setUsername("ADMINISTRATOR");
                    superAdmin.setEmail("administrator@mounanga.com");
                    superAdmin.setPasswordNeedToBeModified(Boolean.TRUE);
                    String defaultPassword = UUID.randomUUID().toString();
                    superAdmin.setPassword(passwordEncoder.encode(defaultPassword));
                    superAdmin.setEnabled(true);
                    User savedUser = userRepository.save(superAdmin);
                    log.info("SUPER ADMIN created successfully with password '{}'", defaultPassword);
                    List<Role> roleList = roleRepository.findAll();
                    savedUser.setRoles(roleList);
                    User updatedUser = userRepository.save(savedUser);
                    log.info("User updated successfully with roles '{}'", updatedUser.getRoles());
                } catch (Exception e) {
                    log.error("Error while creating super admin user", e);
                }
            }
        };
    }
}
