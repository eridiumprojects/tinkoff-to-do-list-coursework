package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.UserMapper;
import com.example.todolistcoursework.model.dto.request.LoginRequest;
import com.example.todolistcoursework.model.dto.request.SignupRequest;
import com.example.todolistcoursework.model.dto.response.JwtResponse;
import com.example.todolistcoursework.model.dto.response.UserInfoResponse;
import com.example.todolistcoursework.model.entity.Device;
import com.example.todolistcoursework.model.entity.RefreshToken;
import com.example.todolistcoursework.model.entity.Role;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.enums.ERole;
import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.model.exception.ClientException;
import com.example.todolistcoursework.model.exception.ServerException;
import com.example.todolistcoursework.repository.DeviceRepository;
import com.example.todolistcoursework.repository.RefreshTokenRepository;
import com.example.todolistcoursework.repository.RoleRepository;
import com.example.todolistcoursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.example.todolistcoursework.model.constant.AuthErrorMessages.INVALID_PASSWORD;
import static com.example.todolistcoursework.model.constant.AuthErrorMessages.USER_DOESNT_EXISTS;
import static com.example.todolistcoursework.model.constant.ClientErrorMessages.EMAIL_ALREADY_TAKEN;
import static com.example.todolistcoursework.model.constant.ClientErrorMessages.USERNAME_ALREADY_TAKEN;
import static com.example.todolistcoursework.model.constant.ErrorMessages.INTERNAL_SERVER_ERROR;

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public JwtResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthException(USER_DOESNT_EXISTS));

        if (user.getPassword() != null && !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException(INVALID_PASSWORD);
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
                tokens.getRefreshToken());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserInfoResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ClientException(USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ClientException(EMAIL_ALREADY_TAKEN);
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository
                .findByName(ERole.USER)
                .orElseThrow(() -> new ServerException(INTERNAL_SERVER_ERROR));
        roles.add(userRole);
        user.setRoles(roles);
        var registeredUser = userRepository.save(user);

        return UserMapper.toApi(registeredUser);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        return UserMapper.toApi(user.get());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserInfoResponse deleteUser(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        userRepository.deleteById(userId);
        return UserMapper.toApi(user.get());
    }
}
