package com.example.todolistcoursework.security;

import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final RedissonClient redissonClient;
    private static final String AUTHORIZATION = "Authorization";
    private final JwtService jwtService;
    @Value("${security.jwt.storage}")
    private String jwtStorageName;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = parseJwt(request);
        JwtService.ClaimsHolder claims = null;
        if (token != null) {
            if (jwtService.validateAccessToken(token)) {
                claims = jwtService.getAccessClaims(token);
            }
        }

        if (claims != null) {
            RMapCache<Long, String> map = redissonClient.getMapCache(jwtStorageName);
            if (token.equals(map.get(Long.parseLong(claims.getDeviceId())))) {
                setSecurityContext(claims);
            } else {
                throw new AuthException("Access Token doesn't refer to this user");
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(JwtService.ClaimsHolder claims) {
        final JwtAuth authInfo =
                JwtAuth.builder()
                        .username(claims.getUsername())
                        .userId(Long.parseLong(claims.getUserId()))
                        .deviceId(Long.parseLong(claims.getDeviceId()))
                        .role(claims.getRole())
                        .authenticated(true)
                        .build();
        SecurityContextHolder.getContext().setAuthentication(authInfo);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
