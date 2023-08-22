package com.mounanga.userservice.repositories;

import com.mounanga.userservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select case when count(r)>0 then true else false END from Role r where r.name=?1")
    Boolean checkIfNameExists(String name);

    @Query("select r from Role r where r.name = ?1")
    Role findByName(String name);
}
