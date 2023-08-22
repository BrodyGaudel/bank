package com.mounanga.userservice.repositories;

import com.mounanga.userservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username=?1")
    User findByUsername(String username);

    @Query("select u from User u")
    Page<User> findAllUsersByPage(Pageable pageable);

    @Query("select case when count(u)>0 then true else false END from User u where u.username=?1")
    Boolean checkIfUsernameExists(String username);
}
