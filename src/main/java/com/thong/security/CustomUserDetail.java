package com.thong.security;

import com.thong.domain.Role;
import com.thong.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Service
public class CustomUserDetail implements UserDetails {

    private User user;

    public String getEmail() {
        return user.getEmail();
    }

    public Boolean getIsDeleted() {
        return user.getIsDeleted();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRoles().stream().map(Role::getName).toString()));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }





}
