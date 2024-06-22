package com.popov.fintrack.wallet.model;

import com.popov.fintrack.wallet.dto.InvitationRequest;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class ConfirmData implements Serializable {
    private final InvitationRequest invitation;
    private final String token;

    public ConfirmData(InvitationRequest invitation) {
        this.invitation = invitation;
        this.token = UUID.randomUUID().toString();
    }
}
