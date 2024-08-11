package org.mounanga.userservice.repository;

import org.mounanga.userservice.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, String> {

    @Query("select v from Verification v where v.email =:email and v.code =:code")
    Optional<Verification> findByEmailAndCode(@Param("email") String email, @Param("code") String code);

    @Query("select v from Verification v where v.email =:email")
    Optional<Verification> findByEmail(@Param("email") String email);

    void deleteByEmail(String email);
}
