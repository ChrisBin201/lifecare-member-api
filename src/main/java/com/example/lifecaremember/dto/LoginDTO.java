package com.example.lifecaremember.dto;

import com.example.lifecaremember.config.UserDetailsInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
public class LoginDTO {
    private long memberNo ;
    private String id;
    private String role;

    private String accessToken;
    private String tokenType ;

    private long expiresIn;

    public LoginDTO(String jwt, UserDetailsInfo userDetails,long expiresIn) {
        this.memberNo = userDetails.getId();
        this.id = userDetails.getUsername();
        this.role = userDetails.getRole();
        this.accessToken = jwt;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
    }
}
