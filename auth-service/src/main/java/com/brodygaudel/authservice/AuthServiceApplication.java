package com.brodygaudel.authservice;

import com.brodygaudel.authservice.entity.Role;
import com.brodygaudel.authservice.entity.User;
import com.brodygaudel.authservice.repository.RoleRepository;
import com.brodygaudel.authservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(UserService userService, RoleRepository roleRepository){
		return args -> {
			List<Role> roleList = roleRepository.findAll();
			List<User> userList = userService.getAllUsers();
			if(roleList.isEmpty() && userList.isEmpty()){
				Role adminRole = new Role(1L, "ADMIN");
				Role userRole = new Role(2L, "USER");
				List<Role> rolesSaved = roleRepository.saveAll(
						List.of(adminRole, userRole)
				);
				User user = User.builder().enabled(true).username("admin").password("admin").roles(rolesSaved).id(1L).build();
				userService.saveUser(user);
			}
		};
	}
}
