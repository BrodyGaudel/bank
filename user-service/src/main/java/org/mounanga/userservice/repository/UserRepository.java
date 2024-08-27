package org.mounanga.userservice.repository;

import org.mounanga.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.profile.firstname like :kw or u.profile.lastname like :kw or u.profile.cin like :kw order by u.profile.firstname asc")
    Page<User> findByFirstnameOrLastnameOrCin(@Param("kw") String keyword, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
