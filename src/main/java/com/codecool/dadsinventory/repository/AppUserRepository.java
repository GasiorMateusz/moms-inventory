package com.codecool.dadsinventory.repository;

import com.codecool.dadsinventory.model.AppUser;
import com.codecool.dadsinventory.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AppUserRepository implements AppUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<AppUser> selectAppUserByUsername(String username) {
        return getAppUsers()
                .stream()
                .filter(appUser -> username.equals((appUser.getUsername())))
                .findFirst();
    }

    private List<AppUser> getAppUsers() {
        List<AppUser> appUsers = new ArrayList<>();
        appUsers.add(new AppUser(UserRole.SON.getGrantedAuthorities(),
                passwordEncoder.encode("son"),
                "son",
                true,
                true,
                true,
                true));
        return appUsers;
    }

}
