package org.mounanga.authenticationservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.authenticationservice.dto.PageResponseDTO;
import org.mounanga.authenticationservice.dto.RoleRequestDTO;
import org.mounanga.authenticationservice.dto.RoleResponseDTO;
import org.mounanga.authenticationservice.entity.Role;
import org.mounanga.authenticationservice.exception.FieldValidationException;
import org.mounanga.authenticationservice.exception.RoleNotFoundException;
import org.mounanga.authenticationservice.repository.RoleRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void createRoleShouldReturnSavedRoleWhenRoleDoesNotExist() {
        // Arrange
        RoleRequestDTO requestDTO = new RoleRequestDTO("ADMIN", "Admin role");
        Role role = new Role();
        role.setName(requestDTO.name());
        role.setDescription(requestDTO.description());

        when(roleRepository.existsByName(requestDTO.name())).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        RoleResponseDTO result = roleService.createRole(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(requestDTO.name(), result.name());
        verify(roleRepository, times(1)).existsByName(requestDTO.name());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void createRoleShouldThrowExceptionWhenRoleAlreadyExists() {
        // Arrange
        RoleRequestDTO requestDTO = new RoleRequestDTO("ADMIN", "Admin role");

        when(roleRepository.existsByName(requestDTO.name())).thenReturn(true);

        // Act & Assert
        assertThrows(FieldValidationException.class, () -> roleService.createRole(requestDTO));
        verify(roleRepository, times(1)).existsByName(requestDTO.name());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void updateRoleShouldUpdateRoleWhenRoleExists() {
        // Arrange
        Long roleId = 1L;
        RoleRequestDTO requestDTO = new RoleRequestDTO("ADMIN", "Updated Admin role");
        Role existingRole = new Role();
        existingRole.setId(roleId);
        existingRole.setName("USER");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(roleRepository.existsByName(requestDTO.name())).thenReturn(false);
        when(roleRepository.save(existingRole)).thenReturn(existingRole);

        // Act
        RoleResponseDTO result = roleService.updateRole(roleId, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(requestDTO.name(), result.name());
        verify(roleRepository, times(1)).findById(roleId);
        verify(roleRepository, times(1)).save(existingRole);
    }

    @Test
    void updateRoleShouldThrowExceptionWhenRoleWithNewNameAlreadyExists() {
        // Arrange
        Long roleId = 1L;
        RoleRequestDTO requestDTO = new RoleRequestDTO("ADMIN", "Updated Admin role");
        Role existingRole = new Role();
        existingRole.setId(roleId);
        existingRole.setName("USER");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(roleRepository.existsByName(requestDTO.name())).thenReturn(true);

        // Act & Assert
        assertThrows(FieldValidationException.class, () -> roleService.updateRole(roleId, requestDTO));
        verify(roleRepository, times(1)).findById(roleId);
        verify(roleRepository, times(1)).existsByName(requestDTO.name());
        verify(roleRepository, never()).save(existingRole);
    }

    @Test
    void deleteRoleByIdShouldDeleteRoleWhenRoleExists() {
        // Arrange
        Long roleId = 1L;

        // Act
        roleService.deleteRoleById(roleId);

        // Assert
        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void getRoleByIdShouldReturnRoleWhenRoleExists() {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        RoleResponseDTO result = roleService.getRoleById(roleId);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.name());
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    void getRoleByIdShouldThrowExceptionWhenRoleDoesNotExist() {
        // Arrange
        Long roleId = 1L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(roleId));
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    void getRoleByNameShouldReturnRoleWhenRoleExists() {
        // Arrange
        String roleName = "ADMIN";
        Role role = new Role();
        role.setName(roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        // Act
        RoleResponseDTO result = roleService.getRoleByName(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(roleName, result.name());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void getRoleByNameShouldThrowExceptionWhenRoleDoesNotExist() {
        // Arrange
        String roleName = "ADMIN";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleByName(roleName));
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void getAllRolesShouldReturnPagedRoles() {
        // Arrange
        int page = 0;
        int size = 10;
        List<Role> roleList = List.of(new Role(), new Role()); // Simule deux rôles
        Page<Role> rolePage = new PageImpl<>(roleList);

        when(roleRepository.findAll(any(Pageable.class))).thenReturn(rolePage);

        // Act
        PageResponseDTO<RoleResponseDTO> result = roleService.getAllRoles(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(roleRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void searchRolesShouldReturnPagedRolesWhenQueryMatches() {
        // Arrange
        String query = "ADMIN";
        int page = 0;
        int size = 10;
        List<Role> roleList = List.of(new Role(), new Role());
        Page<Role> rolePage = new PageImpl<>(roleList);

        when(roleRepository.search(anyString(), any(Pageable.class))).thenReturn(rolePage);

        // Act
        PageResponseDTO<RoleResponseDTO> result = roleService.searchRoles(query, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(roleRepository, times(1)).search(anyString(), any(Pageable.class));
    }

}