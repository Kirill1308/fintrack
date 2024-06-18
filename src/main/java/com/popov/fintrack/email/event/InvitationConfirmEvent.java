package com.popov.fintrack.email.event;

import com.popov.fintrack.wallet.dto.InvitationRequest;

public record InvitationConfirmEvent(InvitationRequest invitation, String token) implements AppEvent {
}
