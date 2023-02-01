package com.codecool.dadsinventory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class securityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public securityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/js/**", "/css/**", "/webjars/**").permitAll() ///js/site.js /webjars/bootstrap...
                .and().authorizeRequests().antMatchers("/item/details/**").hasRole(UserRole.DAD.name())
                .and().authorizeRequests().antMatchers("/home/privacy").hasRole(UserRole.MOM.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
        ;
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails dad = User.builder()
                .username("dad")
                .password(passwordEncoder.encode("dad"))
                .roles(UserRole.DAD.name())
                .build();
        UserDetails mom = User.builder()
                .username("mom")
                .password(passwordEncoder.encode("mom"))
                .roles(UserRole.MOM.name())
                .build();
        UserDetails son = User.builder()
                .username("son")
                .password(passwordEncoder.encode("son"))
                .roles(UserRole.SON.name())
                .build();
        return new InMemoryUserDetailsManager(
                dad, mom, son
        );

    }


}
