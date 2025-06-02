package com.cineflex.api.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {
    String name;
    String[] authorities;

    public static Role[] getRoles() {
        Role user = new Role();
        user.setName("ROLE_USER");
        user.setAuthorities(new String[] {});

        Role moderator = new Role();
        moderator.setName("ROLE_MODERATOR");
        moderator.setAuthorities(new String[] {"DELETE_COMMENT", "ADD_CONTENT", "EDIT_CONTENT"});

        Role admin = new Role();
        admin.setName("ROLE_ADMIN");
        admin.setAuthorities(new String[] {"DELETE_COMMENT", "ADD_CONTENT", "EDIT_CONTENT", "DELETE_CONTENT", "UPGRADE_ROLE"});

        Role[] roles = {user, moderator, admin};

        return roles;
    }
}
