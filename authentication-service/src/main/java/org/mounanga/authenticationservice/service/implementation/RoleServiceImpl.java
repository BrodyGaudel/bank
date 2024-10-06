package org.mounanga.authenticationservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.authenticationservice.dto.PageResponseDTO;
import org.mounanga.authenticationservice.dto.RoleRequestDTO;
import org.mounanga.authenticationservice.dto.RoleResponseDTO;
import org.mounanga.authenticationservice.entity.Role;
import org.mounanga.authenticationservice.exception.FieldValidationException;
import org.mounanga.authenticationservice.exception.RoleNotFoundException;
import org.mounanga.authenticationservice.repository.RoleRepository;
import org.mounanga.authenticationservice.service.RoleService;
import org.mounanga.authenticationservice.util.mapper.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponseDTO createRole(@NotNull RoleRequestDTO roleRequestDTO) {
        log.info("In createRole()");
        if(roleRepository.existsByName(roleRequestDTO.name())){
            throw new FieldValidationException("Invalid data", List.of(String.format("Role with name %s already exists", roleRequestDTO.name())));
        }
        Role role = Mappers.fromRoleRequestDTO(roleRequestDTO);
        Role savedRole = roleRepository.save(role);
        log.info("Role created with name {}", savedRole.getName());
        return Mappers.fromRole(savedRole);
    }

    @Override
    public RoleResponseDTO updateRole(Long id, @NotNull RoleRequestDTO roleRequestDTO) {
        log.info("In updateRole()");
        Role role = findRoleById(id);
        if(!role.getName().equals(roleRequestDTO.name()) && roleRepository.existsByName(roleRequestDTO.name())){
            throw new FieldValidationException("Invalid data", List.of(String.format("Role with name %s already exists", roleRequestDTO.name())));
        }
        role.setName(roleRequestDTO.name());
        role.setDescription(roleRequestDTO.description());
        Role savedRole = roleRepository.save(role);
        log.info("Role with id {} updated", savedRole.getName());
        return Mappers.fromRole(savedRole);
    }

    @Override
    public void deleteRoleById(Long id) {
        log.info("In deleteRoleById()");
        roleRepository.deleteById(id);
        log.info("Role with id {} deleted", id);
    }

    @Override
    public RoleResponseDTO getRoleById(Long id) {
        log.info("In getRoleById()");
        Role role = findRoleById(id);
        log.info("Role with id {} retrieved", id);
        return Mappers.fromRole(role);
    }

    @Override
    public RoleResponseDTO getRoleByName(String name) {
        log.info("In getRoleByName()");
        Role role = roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException(String.format("Role with name %s not found", name)));
        log.info("Role with name {} retrieved", name);
        return Mappers.fromRole(role);
    }

    @Override
    public PageResponseDTO<RoleResponseDTO> getAllRoles(int page, int size) {
        log.info("In getAllRoles()");
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleRepository.findAll(pageable);
        log.info("Roles retrieved {}", roles.getTotalElements());
        return Mappers.fromPageOfRoles(roles);
    }

    @Override
    public PageResponseDTO<RoleResponseDTO> searchRoles(String query, int page, int size) {
        log.info("In searchRoles()");
        Pageable pageable = PageRequest.of(page, size);
        String keyword = "%"+query+"%";
        Page<Role> roles = roleRepository.search(keyword, pageable);
        log.info("Roles retrieved {}.", roles.getTotalElements());
        return Mappers.fromPageOfRoles(roles);
    }

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow( () -> new RoleNotFoundException(String.format("Role with id %s not found", id)));
    }
}
