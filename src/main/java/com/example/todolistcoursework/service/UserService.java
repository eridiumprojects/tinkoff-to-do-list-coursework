package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.UserMapper;
import com.example.todolistcoursework.model.dto.response.JwtResponse;
import com.example.todolistcoursework.model.dto.request.LoginRequest;
import com.example.todolistcoursework.model.dto.request.SignupRequest;
import com.example.todolistcoursework.model.dto.response.UserInfoResponse;
import com.example.todolistcoursework.model.entity.Device;
import com.example.todolistcoursework.model.entity.RefreshToken;
import com.example.todolistcoursework.model.entity.Role;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.enums.ERole;
import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.repository.DeviceRepository;
import com.example.todolistcoursework.repository.RefreshTokenRepository;
import com.example.todolistcoursework.repository.RoleRepository;
import com.example.todolistcoursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final DeviceRepository deviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthException("User doesn't exist"));

        if (user.getPassword() != null && !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException("Error: Invalid password");
        }

        var device = deviceRepository.findByDeviceToken(loginRequest.getDeviceToken());

        if (device.isPresent()) {
            var currentDevice = device.get();
            currentDevice.setLastLogin(Instant.now());
            deviceRepository.save(currentDevice);
        } else {
            var modelDevice = new Device();
            modelDevice.setLastLogin(Instant.now());
            modelDevice.setCreated(Instant.now());
            modelDevice.setDeviceToken(loginRequest.getDeviceToken());
            modelDevice.setUser(user);
            deviceRepository.save(modelDevice);
        }

        var curDevice = deviceRepository.findByDeviceToken(loginRequest.getDeviceToken()).get();
        var tokens = jwtService.generateAccessRefreshTokens(user, curDevice.getId(), ERole.USER);
        var refreshToken = new RefreshToken();
        refreshToken.setToken(tokens.getRefreshToken());
        refreshToken.setUser(user);
        refreshToken.setDevice(curDevice);
        refreshTokenRepository.save(refreshToken);

        return new JwtResponse(
                tokens.getAccessToken(),
                tokens.getRefreshToken(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(e -> e.getName().getAuthority()).toList());
    }

    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository
                .findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    public UserInfoResponse getUserInfo(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException("User doesn't exists");
        }
        return UserMapper.toApi(user.get());
    }

    public UserInfoResponse deleteUser(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException("User doesn't exists");
        }
        userRepository.deleteById(userId);
        return UserMapper.toApi(user.get());
    }
}
