package org.mounanga.userservice.repository;

import org.mounanga.userservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByCin(String cin);
}
