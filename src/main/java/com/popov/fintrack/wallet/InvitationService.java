package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.dto.InvitationRequest;
import com.popov.fintrack.wallet.model.Invitation;

import java.util.List;
import java.util.Optional;

public interface InvitationService {

    List<Invitation> findByWalletId(Long walletId);

    Optional<Invitation> findByToken(String token);

    Optional<Invitation> findByWalletIdAndRecipientId(Long walletId, Long userId);

    Invitation saveInvitation(InvitationRequest request);

    void updateInvitation(Invitation invitation);

    void deleteInvitation(Invitation invitation);

    List<Invitation> getAcceptedInvitations(Long userId);
}
