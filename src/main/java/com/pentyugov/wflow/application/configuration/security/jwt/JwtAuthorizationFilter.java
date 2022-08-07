package com.pentyugov.wflow.application.configuration.security.jwt;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.service.impl.WflowUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.*;


//@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private WflowUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                UUID userId = jwtTokenProvider.getUserIdFromToken(jwt);
                User userDetails = userDetailsService.getUserById(userId);

                Collection<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthorities(jwt);
                userDetails.setAuthorities(authorities);
                userDetails.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            LOG.error("Authentication failed");
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(Security.TOKEN_PREFIX)) {
            return bearToken.substring(Security.TOKEN_PREFIX.length());
        }

        return null;
    }
}
