package org.mounanga.authenticationservice.repository;

import org.mounanga.authenticationservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.firstname like :kw or u.lastname like :kw or u.cin like :kw order by u.firstname asc")
    Page<User> search(@Param("kw") String keyword, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByCin(String cin);

    Optional<User> findByEmail(String email);
}
