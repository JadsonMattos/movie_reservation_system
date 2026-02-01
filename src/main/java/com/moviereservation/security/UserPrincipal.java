package com.moviereservation.security;

import com.moviereservation.domain.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserPrincipal(
        Long id,
        String email,
        String passwordHash,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    public UserPrincipal(Long id, String passwordHash, Collection<? extends GrantedAuthority> authorities) {
        this(id, null, passwordHash, authorities);
    }

    public static UserPrincipal fromJwt(Long id, Collection<? extends GrantedAuthority> authorities) {
        return new UserPrincipal(id, null, null, authorities);
    }

    public static UserPrincipal from(User user) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                authorities);
    }

    public Long getId() {
        return id;
    }

    public boolean isAdmin() {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public String getUsername() {
        return email != null ? email : String.valueOf(id);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
