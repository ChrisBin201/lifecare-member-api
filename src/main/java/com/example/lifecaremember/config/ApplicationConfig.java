package com.example.lifecaremember.config;

import com.example.lifecaremember.model.Member;
import com.example.lifecaremember.model.enumerate.MemberStatus;
import com.example.lifecaremember.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final MemberRepo repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Member> user = repository.findById(username);
            return user.map(u -> {
                if(u.getStatus().name().equals(MemberStatus.INACTIVE.name())) {
                    throw new UsernameNotFoundException("Member not found");
                } else {
                    return new UserDetailsInfo(u);
                }
            }).orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
