package org.mounanga.authenticationservice.repository;

import org.mounanga.authenticationservice.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, String> {

    Optional<Verification> findByEmail(String email);

    @Query("select v from Verification v where v.email =:email and v.code =:code")
    Optional<Verification> findByEmailAndCode(@Param("code") String code, @Param("email") String email);
}
