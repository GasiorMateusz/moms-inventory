package com.codecool.dadsinventory.app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppGrantedAuthorityRepository extends JpaRepository<AppSimpleGrantedAuthority, Long> {
}
