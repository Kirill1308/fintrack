package com.popov.fintrack.user;

import com.popov.fintrack.user.dto.auth.JwtRequest;
import com.popov.fintrack.user.dto.auth.JwtResponse;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.utills.validation.OnCreate;
import com.popov.fintrack.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "API for managing authentications")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody final JwtRequest loginRequest) {
        log.info("Attempting to log in user with username: {}", loginRequest.getUsername());
        JwtResponse response = authService.login(loginRequest);
        log.info("User {} logged in successfully", loginRequest.getUsername());
        return response;
    }

    @Operation(summary = "User registration", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/register")
    public UserDTO register(@Validated(OnCreate.class) @RequestBody final UserDTO userDTO) {
        log.info("Attempting to register user with username: {}", userDTO.getUsername());
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.createUser(user);
        UserDTO createdUserDTO = userMapper.toDto(createdUser);
        log.info("User {} registered successfully", userDTO.getUsername());
        return createdUserDTO;
    }

    @Operation(summary = "Refresh JWT token", description = "Refresh JWT token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful token refresh",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody final String refreshToken) {
        log.info("Attempting to refresh JWT token");
        JwtResponse response = authService.refresh(refreshToken);
        log.info("JWT token refreshed successfully");
        return response;
    }
}
