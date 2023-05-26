package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.dto.request.RefreshRequest;
import com.example.todolistcoursework.model.dto.response.RefreshResponse;
import com.example.todolistcoursework.model.entity.Device;
import com.example.todolistcoursework.model.entity.RefreshToken;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.enums.ERole;
import com.example.todolistcoursework.repository.DeviceRepository;
import com.example.todolistcoursework.repository.RefreshTokenRepository;
import com.example.todolistcoursework.repository.UserRepository;
import com.example.todolistcoursework.security.JwtAuth;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    JwtService jwtService;
    @Mock
    UserRepository userRepository;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    JwtAuth jwtAuth;
    @Mock
    Authentication authenticationMock;
    @InjectMocks
    AuthService authService;

    public RefreshRequest createRefreshRequestByDefault() {
        new RefreshRequest().setRefreshToken(UUID.randomUUID().toString());
        return new RefreshRequest();
    }

    public User createUserByDefault() {
        return User.builder()
                .id(1L)
                .firstName("Anton")
                .lastName("Pestrikov")
                .username("eridium")
                .email("pestrikov@mail.ru")
                .password("pestrikov123")
                .devices(List.of())
                .build();
    }

    public Device createDeviceByDefault() {
        Device device = new Device();
        device.setDeviceToken(UUID.randomUUID());
        device.setId(1L);
        device.setUser(createUserByDefault());
        device.setCreated(Instant.now());
        device.setLastLogin(Instant.now()) ;
        return device;
    }

    public RefreshToken createRefreshTokenByDefault() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(createUserByDefault());
        refreshToken.setDevice(createDeviceByDefault());
        refreshToken.setId(1L);
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshToken;
    }

    public RefreshResponse createRefreshResponseByDefault() {
        return new RefreshResponse(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
    }

    @BeforeEach
    void initSecurityContextHolder() {
        when(authenticationMock.getCredentials()).thenReturn("mock");
        SecurityContextHolder.getContext().setAuthentication(authenticationMock);
    }
    @Test
    void getJwtAuth_success() {
        SecurityContextHolder.getContext().getAuthentication().getCredentials();
        JwtAuth afterResponse = authService.getJwtAuth();
        assertNotNull(afterResponse);
    }

    @WithMockUser(username = "admin", authorities = "ADMIN")
    @Test
    void refreshToken_success() {

        //given
        var request = createRefreshRequestByDefault();
        JwtService.ClaimsHolder claims = jwtService.getRefreshClaims(request.getRefreshToken());
        var user = createUserByDefault();
        var device = createDeviceByDefault();
        var refreshToken = createRefreshTokenByDefault();
        var refreshResponse = createRefreshResponseByDefault();
        //when
        when(jwtService.validateRefreshToken(request.getRefreshToken())).thenReturn(true);
        when(jwtService.getRefreshClaims(request.getRefreshToken())).thenReturn(claims);
        when(claims.getUserId()).thenReturn(String.valueOf(user));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(deviceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(device));
        when(refreshTokenRepository.findByToken(request.getRefreshToken())).thenReturn(Optional.ofNullable(refreshToken));
        when(jwtService.validateAccessTokenLifetime(1L)).thenReturn(true);
        when(jwtService.generateAccessRefreshTokens(user,1L, ERole.valueOf("ADMIN"))).thenReturn(refreshResponse);
//        when(jwtService.generateAccessRefreshTokens(any(),anyLong(),any())).thenReturn(refreshResponse);

        RefreshResponse afterResponse = authService.refresh(request);
        //then
        verify(refreshTokenRepository,times(1)).deleteById(1L);
        verify(refreshTokenRepository,times(1)).save(any(RefreshToken.class));


    }
}