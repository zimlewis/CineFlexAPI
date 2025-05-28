package com.cineflex.API.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails{

    private final Account account;

    public UserPrincipal (
        Account account
    ) {
        this.account = account;
    }

    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return account.getVerify();
    }
    
}
