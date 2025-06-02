package com.cineflex.api.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cineflex.api.model.Account;

public class UserPrincipal implements UserDetails{

    private final Account account;

    public UserPrincipal (
        Account account
    ) {
        this.account = account;
    }


    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authoritiesStrings = Arrays.asList(Role.getRoles()[account.getRole()].authorities);
        List<SimpleGrantedAuthority> authorities = authoritiesStrings.stream()
            .map(a -> new SimpleGrantedAuthority(a))
            .toList();
        return authorities;
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
