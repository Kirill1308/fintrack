package com.popov.fintrack.wallet.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class InvitationRequest {
    private Long walletId;
    private Long senderId;
    @Email
    private String recipientEmail;
}
