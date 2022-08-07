package com.pentyugov.wflow.application.configuration.security.jwt;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Security;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final UserService userService;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) throws UserNotFoundException {
        User user = (User) authentication.getPrincipal();
        user = userService.getById(user.getId());
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + Security.EXPIRATION_TIME);

        String userId = user.getId().toString();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getUsername());
        claimsMap.put(Security.AUTHORITIES, authorities);

        userService.updateLastLoginDate(user);
        return Jwts
                .builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setAudience(Security.GET_ARRAYS_ADMINISTRATION)
                .setIssuer(Security.GET_ARRAYS_LLC)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException
                | MalformedJwtException
                | ExpiredJwtException
                | UnsupportedJwtException
                | IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();
        return UUID.fromString((String) claims.get("id"));
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();
        return Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

}
