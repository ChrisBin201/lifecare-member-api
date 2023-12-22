package com.example.lifecaremember.config;

import com.example.lifecaremember.model.Member;
import com.example.lifecaremember.model.enumerate.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsInfo implements UserDetails {
    private long id ;
    private String username;
    private String password;
    private String role;
    private List<GrantedAuthority> authorities;

    public UserDetailsInfo(Member user) {
        id = user.getMemberNo();
        username = user.getId();
        password = user.getPassword();
        role = user.getRole().name();
        authorities= user.getRole().name().equals(Role.ADMIN.name()) ?
                Stream.of(new SimpleGrantedAuthority(Role.ADMIN.name())).collect(Collectors.toList())
                :
                user.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode().name()))
                .collect(Collectors.toList());
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
        return username;
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
