
---

# Authentication Service

## Overview
The **Authentication Service** handles user management, role assignment, and authentication processes. It utilizes **Spring Security** for securing endpoints and managing user sessions, while **Java JWT** is employed for token-based authentication. This service ensures that only authenticated and authorized users can access specific resources.

This service is built with **Java 21**, **Spring Boot**, and **Spring Security**.

## Technologies Used
- **Java 21**
- **Spring Boot** 3.3.4
- **Spring Security** for authentication and authorization
- **Java JWT** for token management
- **Maven**

## Default Roles and Super Admin Creation
When the application starts, it will check if the roles USER, ADMIN, and SUPER_ADMIN exist in the database. If they do not exist, they will be created automatically.
Similarly, if a super admin user does not exist, a user with the username ADMINISTRATOR will be created with a randomly generated password.
you must update this default password. 

## API Endpoints
### User Management
The **UserRestController** exposes several endpoints for user management:

```java
@RestController
@RequestMapping("/users")
public class UserRestController {
    ...
}
```

#### Endpoints
| HTTP Method | Endpoint               | Description                                               | Roles Required         |
|-------------|------------------------|-----------------------------------------------------------|------------------------|
| POST        | `/users/create`        | Create a new user                                         | ADMIN, SUPER_ADMIN     |
| PUT         | `/users/update/{id}`   | Update an existing user                                   | ADMIN, SUPER_ADMIN     |
| POST        | `/users/pwd`           | Update the password for the current user                 | USER, ADMIN, SUPER_ADMIN|
| GET         | `/users/status/{id}`   | Update the status of a user                              | USER, ADMIN, SUPER_ADMIN|
| DELETE      | `/users/delete/{id}`   | Delete a user by ID                                      | ADMIN, SUPER_ADMIN     |
| POST        | `/users/roles/add`     | Add a role to a user                                     | ADMIN, SUPER_ADMIN     |
| POST        | `/users/roles/remove`  | Remove a role from a user                                | ADMIN, SUPER_ADMIN     |
| GET         | `/users/get/{id}`      | Get user details by ID                                   | USER, ADMIN, SUPER_ADMIN|
| GET         | `/users/profile`       | Get the details of the currently logged-in user          | USER, ADMIN, SUPER_ADMIN|
| GET         | `/users/all`           | Get a paginated list of all users                        | ADMIN, SUPER_ADMIN     |
| GET         | `/users/search`        | Search for users based on a query                        | ADMIN, SUPER_ADMIN     |

### Role Management
The **RoleRestController** manages roles associated with users:

```java
@RestController
@RequestMapping("/roles")
public class RoleRestController {
    ...
}
```

#### Endpoints
| HTTP Method | Endpoint                 | Description                                         | Roles Required         |
|-------------|--------------------------|-----------------------------------------------------|------------------------|
| POST        | `/roles/create`          | Create a new role                                  | ADMIN, SUPER_ADMIN     |
| PUT         | `/roles/update/{id}`     | Update an existing role                            | ADMIN, SUPER_ADMIN     |
| DELETE      | `/roles/delete/{id}`     | Delete a role by ID                               | ADMIN, SUPER_ADMIN     |
| GET         | `/roles/get/{id}`        | Get role details by ID                            | ADMIN, SUPER_ADMIN     |
| GET         | `/roles/find/{name}`     | Get role details by name                          | ADMIN, SUPER_ADMIN     |
| GET         | `/roles/all`             | Get a paginated list of all roles                 | ADMIN, SUPER_ADMIN     |
| GET         | `/roles/search`          | Search for roles based on a query                 | ADMIN, SUPER_ADMIN     |

### Password Management
The **PasswordRestController** handles password reset requests:

```java
@RestController
@RequestMapping("/passwords")
public class PasswordRestController {
    ...
}
```

#### Endpoints
| HTTP Method | Endpoint                 | Description                                          |
|-------------|--------------------------|------------------------------------------------------|
| GET         | `/passwords/ask/{email}` | Request a code to reset password for the specified email |
| POST        | `/passwords/reset`       | Reset the password using the provided code          |

### Authentication
The **AuthenticationRestController** manages user login:

```java
@RestController
@RequestMapping("/authentication")
public class AuthenticationRestController {
    ...
}
```

#### Endpoints
| HTTP Method | Endpoint                 | Description                                          |
|-------------|--------------------------|------------------------------------------------------|
| POST        | `/authentication/login`   | Authenticate a user and return JWT token            |

## Data Transfer Objects (DTOs)

### User DTOs
- **UserResponseDTO**: Contains details of a user.
- **UserRequestDTO**: Used to create or update a user.
- **UpdatePasswordRequestDTO**: Used to update user password.

### Role DTOs
- **RoleResponseDTO**: Contains details of a role.
- **RoleRequestDTO**: Used to create or update a role.

### Login DTOs
- **LoginResponseDTO**: Contains JWT and password update requirement.
- **LoginRequestDTO**: Contains user credentials for authentication.

### Change Password DTO
- **ChangePasswordRequestDTO**: Used to reset the user's password.

### Page Response DTO
- **PageResponseDTO**: A generic response class for paginated data.

## Security Configuration
The application uses **Spring Security** annotations like `@PreAuthorize` to enforce security at the method level. Access to certain endpoints is restricted based on user roles (e.g., `ADMIN`, `USER`, `SUPER_ADMIN`).

### Example of Role-based Access Control
```java
@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
@PostMapping("/create")
public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
    return userService.createUser(dto);
}
```

## Conclusion
The **Authentication Service** provides a comprehensive solution for user management and authentication. With robust role-based access control and JWT integration, it ensures secure and efficient user interactions within the application.

---
