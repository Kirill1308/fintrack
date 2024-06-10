package com.popov.fintrack.event;

import com.popov.fintrack.wallet.dto.InvitationRequest;

public record InvitationConfirmEvent(InvitationRequest invitation, String token) implements AppEvent {
}
