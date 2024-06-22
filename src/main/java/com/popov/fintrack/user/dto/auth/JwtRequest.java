package com.popov.fintrack.user.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request object for user authentication containing the username and password.")
public class JwtRequest {

    @Schema(description = "Username of the user", example = "johndoe@gmail.com")
    @NotNull(message = "Username cannot be null")
    private String username;

    @Schema(description = "Password of the user", example = "12345")
    @NotNull(message = "Password cannot be null")
    private String password;
}
