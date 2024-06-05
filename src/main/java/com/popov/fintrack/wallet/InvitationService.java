package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.dto.InvitationRequest;
import com.popov.fintrack.wallet.model.Invitation;

import java.util.Optional;

public interface InvitationService {

    Optional<Invitation> findByToken(String token);

    Optional<Invitation> findInvitation(Long walletId, Long recipientId);

    Invitation saveInvitation(InvitationRequest request);

    void updateInvitation(Invitation invitation);

    void deleteInvitation(Invitation invitation);
}
