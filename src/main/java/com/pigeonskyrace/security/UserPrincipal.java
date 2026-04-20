package com.pigeonskyrace.security;

import com.pigeonskyrace.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private final String id;
    private final String email;
    private final String password;
    private final Role role;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(
            String id,
            String email,
            String password,
            Role role,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
