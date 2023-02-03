package com.codecool.dadsinventory.service;

import com.codecool.dadsinventory.auth.AuthGrantedAuthority;
import com.codecool.dadsinventory.auth.AuthUserDetails;
import com.codecool.dadsinventory.auth.AuthUserDetailsRepository;
import com.codecool.dadsinventory.model.Category;
import com.codecool.dadsinventory.model.Item;
import com.codecool.dadsinventory.repository.CategoryRepository;
import com.codecool.dadsinventory.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class InitService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserDetailsRepository authUserDetailsRepository;
    private final AuthGrantedAuthorityRepository authGrantedAuthorityRepository;

    @Autowired
    public InitService(ItemRepository itemRepository, CategoryRepository categoryRepository, PasswordEncoder passwordEncoder, AuthUserDetailsRepository authUserDetailsRepository, AuthGrantedAuthorityRepository authGrantedAuthorityRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUserDetailsRepository = authUserDetailsRepository;
        this.authGrantedAuthorityRepository = authGrantedAuthorityRepository;
    }

    public void seedDatabase() {
        Category smallCat = Category.builder().name("small").build();
        Category mediumCat = Category.builder().name("medium").build();
        Category largeCat = Category.builder().name("medium").build();

        smallCat = categoryRepository.saveAndFlush(smallCat);
        mediumCat = categoryRepository.saveAndFlush(mediumCat);
        largeCat = categoryRepository.saveAndFlush(largeCat);

        Item ship = Item.builder().name("Ship").price(5).category(largeCat).comment("").inStock(true).build();
        Item hammer = Item.builder().name("Hammer").price(3).category(mediumCat).comment("").inStock(true).build();
        Item pin = Item.builder().name("Pin").price(2).category(smallCat).comment("").inStock(true).build();
        ship = itemRepository.saveAndFlush(ship);
        hammer = itemRepository.saveAndFlush(hammer);
        pin = itemRepository.saveAndFlush(pin);

        smallCat.setItems(List.of(pin));
        mediumCat.setItems(List.of(hammer));
        largeCat.setItems(List.of(ship));
        categoryRepository.saveAllAndFlush(Arrays.asList(smallCat, mediumCat, largeCat));

    }

    // initialize the user in DB
    @Bean
    public CommandLineRunner initializeJpaData() {
        return (args) -> {
            System.out.println("application started");

            //uncomment if required

            AuthUserDetails user2 = new AuthUserDetails();
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("password"));
            user2.setEnabled(true);
            user2.setCredentialsNonExpired(true);
            user2.setAccountNonExpired(true);
            user2.setAccountNonLocked(true);

            AuthGrantedAuthority grantedAuthority = new AuthGrantedAuthority();
            grantedAuthority.setAuthority("USER");
            grantedAuthority.setAuthUserDetails(user2);
            authUserDetailsRepository.save(user2);
            authGrantedAuthorityRepository.saveAndFlush(grantedAuthority);
            user2.setAuthorities(Collections.singleton(grantedAuthority));
        };


    }

}
