package com.popov.fintrack.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response object for wallet invitation containing the accept token")
public class InvitationResponse {

    @Schema(description = "Token used to accept the invitation", example = "abcdef123456")
    private String acceptToken;
}
