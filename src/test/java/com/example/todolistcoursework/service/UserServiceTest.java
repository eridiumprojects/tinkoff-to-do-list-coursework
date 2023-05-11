package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.UserMapper;
import com.example.todolistcoursework.model.dto.request.LoginRequest;
import com.example.todolistcoursework.model.dto.request.SignupRequest;
import com.example.todolistcoursework.model.dto.response.JwtResponse;
import com.example.todolistcoursework.model.dto.response.RefreshResponse;
import com.example.todolistcoursework.model.dto.response.UserInfoResponse;
import com.example.todolistcoursework.model.entity.Device;
import com.example.todolistcoursework.model.entity.RefreshToken;
import com.example.todolistcoursework.model.entity.Role;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.enums.ERole;
import com.example.todolistcoursework.repository.DeviceRepository;
import com.example.todolistcoursework.repository.RefreshTokenRepository;
import com.example.todolistcoursework.repository.RoleRepository;
import com.example.todolistcoursework.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    JwtService jwtService;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void loginUser() {
        User user = new User();
        Device device = new Device();
        device.setUser(user);
        device.setId(1L);
        device.setDeviceToken(UUID.randomUUID());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceToken(device.getDeviceToken());
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        user.setId(1L);
        user.setPassword("password");
        RefreshResponse refreshResponse = new RefreshResponse(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
        user.setPassword("password");
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(deviceRepository.findByDeviceToken(loginRequest.getDeviceToken())).thenReturn(Optional.ofNullable(device));
        when(jwtService.generateAccessRefreshTokens(user,device.getId(), ERole.USER)).thenReturn(refreshResponse);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setDevice(device);
        refreshToken.setToken(refreshResponse.getRefreshToken());

        userService.loginUser(loginRequest);

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void registerUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("username");
        signupRequest.setPassword("password");
        signupRequest.setUsername("username");
        signupRequest.setEmail("email@mail.ru");
        signupRequest.setFirstName("first");
        signupRequest.setLastName("last");
        User user = new User();
        user.setId(1L);
        user.setLastName("l");
        user.setFirstName("f");
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.USER);
        user.setUsername("username");
        user.setPassword("password");
        user.setRoles(Set.of(role));
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                "a","a","a","a",Set.of());
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(signupRequest.getPassword());
        when(roleRepository.findByName(ERole.USER)).thenReturn(Optional.ofNullable(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        try (MockedStatic mockedStatic = mockStatic(UserMapper.class)){
            mockedStatic.when(() -> UserMapper.toApi(user)).thenReturn(userInfoResponse);
        }

        userService.registerUser(signupRequest);

        verify(userRepository).save(any(User.class));
    }


    @Test
    void getUserInfo() {
        User user = new User();
        user.setFirstName("f");
        user.setLastName("l");
        user.setRoles(Set.of());
        user.setId(1L);
        user.setEmail("mail");

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserInfoResponse userInfoResponse = userService.getUserInfo(user.getId());

        assertEquals(user.getUsername(),userInfoResponse.getUsername());
        assertNotNull(userInfoResponse);
    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setFirstName("f");
        user.setLastName("l");
        user.setRoles(Set.of());
        user.setId(1L);
        user.setEmail("mail");

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        userService.deleteUser(user.getId());

        verify(userRepository).deleteById(user.getId());
    }
}