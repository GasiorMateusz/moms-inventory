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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .formLogin() //store sessionID in postgres, now it is in guess in memory
                .loginPage("/login").permitAll() // change basic login form
                .defaultSuccessUrl("/", true) //default redirect. I guess it might be getter without this
                .usernameParameter("username") //can change parameters name from default
                .and()
                .rememberMe().rememberMeCookieName("remember-me").tokenValiditySeconds(6000) //default 2 weeks //change to save in db
                //rememberMe cookie name must equal to checkbox name
//                .key("") can override default key
                .and()
                .logout()
                .logoutUrl("/logout") //use POST to logout and HTTPS with csrf
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) //use it if not using csrf
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login")
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
