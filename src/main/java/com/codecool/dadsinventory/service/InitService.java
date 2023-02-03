package com.codecool.dadsinventory.service;

import com.codecool.dadsinventory.app.AppGrantedAuthorityRepository;
import com.codecool.dadsinventory.app.AppSimpleGrantedAuthority;
import com.codecool.dadsinventory.app.AppUserDetails;
import com.codecool.dadsinventory.app.AppUserDetailsRepository;
import com.codecool.dadsinventory.model.Category;
import com.codecool.dadsinventory.model.Item;
import com.codecool.dadsinventory.repository.CategoryRepository;
import com.codecool.dadsinventory.repository.ItemRepository;
import com.codecool.dadsinventory.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class InitService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    private final AppGrantedAuthorityRepository appGrantedAuthorityRepository;
    private final AppUserDetailsRepository appUserDetailsRepository;

    @Autowired
    public InitService(ItemRepository itemRepository, CategoryRepository categoryRepository, PasswordEncoder passwordEncoder, AppGrantedAuthorityRepository appGrantedAuthorityRepository, AppUserDetailsRepository appUserDetailsRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.appGrantedAuthorityRepository = appGrantedAuthorityRepository;
        this.appUserDetailsRepository = appUserDetailsRepository;
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

            AppUserDetails son = new AppUserDetails();
            son.setUsername("son");
            son.setPassword(passwordEncoder.encode("son"));
            son.setEnabled(true);
            son.setCredentialsNonExpired(true);
            son.setAccountNonExpired(true);
            son.setAccountNonLocked(true);
            son.setRole(UserRole.SON.name());
            List<AppSimpleGrantedAuthority> sonAuthorities = UserRole.SON.getGrantedAuthorities(son); //todo refactor UserRole.getAuthorities to stop duplicating permissions
            son.setAuthorities(sonAuthorities);
            appUserDetailsRepository.saveAndFlush(son);
            for (AppSimpleGrantedAuthority authority : sonAuthorities
            ) {
                appGrantedAuthorityRepository.saveAndFlush(authority);
            }
        };


    }

}
