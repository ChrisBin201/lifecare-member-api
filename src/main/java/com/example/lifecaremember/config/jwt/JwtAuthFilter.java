package com.example.lifecaremember.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsServiceImpl;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader("Authorization");
        String reqUri = request.getRequestURI();
        String method = request.getMethod();

        String username = null;
        String jwtToken = null;
        try {
            if (requestToken != null) {
                if (requestToken.startsWith("Bearer")) {
                    jwtToken = requestToken.substring(7);
                    try {
                        username = jwtTokenProvider.getUsernameFromToken(jwtToken);
                    } catch (IllegalArgumentException e) {
                        logger.error("Unable to get JWT token", e);
                    } catch (ExpiredJwtException e) {
                        logger.error("JWT Token has expired", e);
                    }

                } else {
                    logger.warn("JWT Token doesn't begin with Bearer string");
                }

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof UserDetails) {
                        logger.info("------SecurityContextHolder getPrincipal UserDetails:" + ((UserDetails) principal).getUsername());
                    } else {
                        logger.info("------SecurityContextHolder getPrincipal :" + principal);
                    }
                }

                //once we get the token validate it
                if (username != null && (authentication == null || "anonymousUser".equalsIgnoreCase((String) authentication.getPrincipal()))) {
                    UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
                    log.info("userDetails: " + userDetails.getAuthorities().toString());

                    // if token is valid configure Spring security to manually set authentication
                    if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        ;
                        // after setting the authentication in the context, we specify that
                        // the current user is authenticated. So it passes the spring security configuration successfully
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    }
                }
            }
//            else {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
//            }

            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(request, responseWrapper);

            // copy to body response
            responseWrapper.copyBodyToResponse();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


    }
}