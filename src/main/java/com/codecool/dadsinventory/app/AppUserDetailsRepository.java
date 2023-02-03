package com.codecool.dadsinventory.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserDetailsRepository extends JpaRepository<AppUserDetails, Long> {
    Optional<AppUserDetails> findByUsername(String username);

    Optional<AppUserDetails> findByPassword(String password);
}