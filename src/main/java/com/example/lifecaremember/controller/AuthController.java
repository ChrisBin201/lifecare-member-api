package com.example.lifecaremember.controller;

import com.example.lifecaremember.config.UserDetailsInfo;
import com.example.lifecaremember.config.jwt.JwtTokenProvider;
import com.example.lifecaremember.dto.LoginDTO;
import com.example.lifecaremember.dto.ResponseData;
import com.example.lifecaremember.dto.payload.LoginPayload;
import com.example.lifecaremember.handler.CommonErrorCode;
import com.example.lifecaremember.handler.CommonException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginPayload payload) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    payload.getId(),
                    payload.getPassword())
            );

            List<String> permissions = new ArrayList<>();
            UserDetailsInfo userDetails = null;
            String jwt = null;
            String role = null;

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    userDetails = (UserDetailsInfo) principal;
                    permissions = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
                    role = userDetails.getRole();
                    jwt = jwtTokenProvider.generateToken(userDetails.getUsername(), role, permissions);
                    LoginDTO loginDTO = new LoginDTO(jwt, userDetails, jwtTokenProvider.getExpirationDateFromToken(jwt).getTime());
                    ResponseData<LoginDTO> response = new ResponseData<>();
                    response.initData(loginDTO);

                    return ResponseEntity.ok().body(response);
                }
            }
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_USERNAME_PASSWORD.getCode(), CommonErrorCode.INVALID_USERNAME_PASSWORD.getMessage());



        } catch (BadCredentialsException ex) {
            log.error(ex.getMessage(), ex);
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_USERNAME_PASSWORD.getCode(), CommonErrorCode.INVALID_USERNAME_PASSWORD.getMessage());

        } catch (UsernameNotFoundException ex) {
            log.error(ex.getMessage(), ex);
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.USERNAME_NOT_FOUND.getCode(), CommonErrorCode.USERNAME_NOT_FOUND.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.SYSTEM_ERROR.getCode(), CommonErrorCode.SYSTEM_ERROR.getMessage());
        }

    }
}
