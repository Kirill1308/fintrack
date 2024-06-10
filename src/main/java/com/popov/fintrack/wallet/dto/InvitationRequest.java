package com.popov.fintrack.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Schema(description = "Request object for sending wallet invitations")
public class InvitationRequest implements Serializable {

    @Schema(description = "ID of the wallet to which the invitation is sent", example = "1")
    @NotNull(message = "Wallet ID cannot be null")
    private Long walletId;

    @Schema(description = "ID of the sender", example = "2")
    @NotNull(message = "Sender ID cannot be null")
    private Long senderId;

    @Schema(description = "Email of the recipient", example = "recipient@example.com")
    @Email(message = "Recipient email should be valid")
    private String recipientEmail;
}

