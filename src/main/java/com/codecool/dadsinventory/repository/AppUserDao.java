package com.codecool.dadsinventory.repository;

import com.codecool.dadsinventory.model.AppUser;

import java.util.Optional;

public interface AppUserDao {

    Optional<AppUser> selectAppUserByUsername(String username);

}
