package org.mounanga.authenticationservice.service;

import org.mounanga.authenticationservice.dto.PageResponseDTO;
import org.mounanga.authenticationservice.dto.RoleRequestDTO;
import org.mounanga.authenticationservice.dto.RoleResponseDTO;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);
    RoleResponseDTO updateRole(Long id, RoleRequestDTO roleRequestDTO);
    void deleteRoleById(Long id);
    RoleResponseDTO getRoleById(Long id);
    RoleResponseDTO getRoleByName(String name);
    PageResponseDTO<RoleResponseDTO> getAllRoles(int page, int size);
    PageResponseDTO<RoleResponseDTO> searchRoles(String query, int page, int size);
}
