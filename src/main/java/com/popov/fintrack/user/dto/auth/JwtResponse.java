package com.popov.fintrack.user.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response object containing JWT tokens and user information.")
public class JwtResponse {

    @Schema(description = "ID of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "johndoe@gmail.com")
    private String username;

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "JWT refresh token", example = "dGhpc2lzdGhlcmVmcmVzaHRva2Vu...")
    private String refreshToken;
}
