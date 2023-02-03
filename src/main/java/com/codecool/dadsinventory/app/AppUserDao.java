package com.codecool.dadsinventory.app;

import java.util.Optional;

public interface AppUserDao {

    Optional<AppUser> selectAppUserByUsername(String username);

}
