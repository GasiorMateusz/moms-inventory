package com.codecool.dadsinventory.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class AppSimpleGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 550L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String authority;
    @ManyToOne
    @JoinColumn(name = "app_user_detail_id")
    private AppUserDetails appUserDetails;

    public AppSimpleGrantedAuthority(String authority, AppUserDetails user) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
        this.appUserDetails = user;
    }

    public AppSimpleGrantedAuthority() {

    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof AppSimpleGrantedAuthority && this.authority.equals(((AppSimpleGrantedAuthority) obj).authority);
        }
    }

    //constructors
    //getters and setters

}