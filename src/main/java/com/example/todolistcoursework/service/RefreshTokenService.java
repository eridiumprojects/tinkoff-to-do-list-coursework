package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.dto.RefreshRequest;
import com.example.todolistcoursework.model.dto.RefreshResponse;
import com.example.todolistcoursework.model.entity.RefreshToken;
import com.example.todolistcoursework.model.exception.TokenRefreshException;
import com.example.todolistcoursework.repository.RefreshTokenRepository;
import com.example.todolistcoursework.repository.UserRepository;
import com.example.todolistcoursework.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final Long refreshTokenDurationMs = 86400000L;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public RefreshTokenService(UserRepository userRepository,
                               JwtUtils jwtUtils,
                               RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public ResponseEntity<?> refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        return findByToken(refreshToken).map(this::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            String token = jwtUtils.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new RefreshResponse(token, refreshToken));
        }).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
