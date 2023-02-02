package com.codecool.dadsinventory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@EnableGlobalMethodSecurity(securedEnabled = true) // needed to use annotation authorize with preAuthorize()
public class securityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public securityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //to enable csrf client has to send back header : X-XSRF-TOKEN with value it get from server
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//unable to acces token with js
//                .and() //enabled csrf token
                .authorizeRequests()
                .antMatchers("/", "/js/**", "/css/**", "/webjars/**").permitAll() ///js/site.js /webjars/bootstrap...
                .antMatchers("/item/details/**").hasAuthority(UserPermission.DETAILS.getPermission())
                .antMatchers("/home/privacy").hasAnyRole(UserRole.SON.name(), UserRole.MOM.name())
                .antMatchers(HttpMethod.GET, "/management/**").hasAuthority(UserPermission.READER.getPermission())
                .antMatchers(HttpMethod.POST, "/management/**").hasAuthority(UserPermission.EDITOR.getPermission())
                .antMatchers(HttpMethod.GET, "/management/items").hasAuthority(UserPermission.EDITOR.getPermission())
                //can substitute above with : @preAuthorize("hasAuthority(privacy:read)") before mapping
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
//                .roles(UserRole.DAD.name())
                .authorities(UserRole.DAD.getGrantedAuthorities())
                .build();
        UserDetails mom = User.builder()
                .username("mom")
                .password(passwordEncoder.encode("mom"))
//                .roles(UserRole.MOM.name())
                .authorities(UserRole.MOM.getGrantedAuthorities())
                .build();
        UserDetails son = User.builder()
                .username("son")
                .password(passwordEncoder.encode("son"))
//                .roles(UserRole.SON.name())
                .authorities(UserRole.SON.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(
                dad, mom, son
        );

    }


}
