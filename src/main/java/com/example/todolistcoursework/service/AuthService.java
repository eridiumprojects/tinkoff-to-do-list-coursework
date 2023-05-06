package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.constant.ErrorMessagePool;
import com.example.todolistcoursework.model.dto.request.RefreshRequest;
import com.example.todolistcoursework.model.dto.response.RefreshResponse;
import com.example.todolistcoursework.model.entity.RefreshToken;
import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.repository.DeviceRepository;
import com.example.todolistcoursework.repository.RefreshTokenRepository;
import com.example.todolistcoursework.repository.UserRepository;
import com.example.todolistcoursework.security.JwtAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.example.todolistcoursework.model.constant.ErrorMessagePool.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuth getJwtAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuth) {
            return ((JwtAuth) authentication);
        } else {
            return JwtAuth.builder().build();
        }
    }

    public RefreshResponse refresh(RefreshRequest request) {
        if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
            throw new AuthException(INVALID_REFRESH_TOKEN);
        }

        var claims = jwtService.getRefreshClaims(request.getRefreshToken());
        var userId = Long.parseLong(claims.getUserId());
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(USER_NOT_FOUND));
        var deviceId = Long.parseLong(claims.getDeviceId());
        var device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new AuthException(DEVICE_NOT_FOUND));
        var role = claims.getRole();

        var currentRefreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());

        if (currentRefreshToken.isEmpty()) {
            throw new AuthException(REFRESH_TOKEN_DOESNT_EXISTS);
        }

        refreshTokenRepository.deleteById(currentRefreshToken.get().getId());
        if (!jwtService.validateAccessTokenLifetime(device.getId())) {
            throw new AuthException(SUSPICIOUS_ACTIVITY);
        }

        var tokens = jwtService.generateAccessRefreshTokens(user, device.getId(), role);
        var newRefresh = new RefreshToken();
        newRefresh.setUser(user);
        newRefresh.setDevice(device);
        newRefresh.setToken(tokens.getRefreshToken());

        refreshTokenRepository.save(newRefresh);

        return tokens;
    }
}