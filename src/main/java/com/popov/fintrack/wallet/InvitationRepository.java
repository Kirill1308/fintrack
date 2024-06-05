package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.model.Invitation;
import com.popov.fintrack.wallet.model.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByToken(String token);

    List<Invitation> findByWalletIdAndStatus(Long walletId, InvitationStatus status);

    List<Invitation> findByRecipientIdAndStatus(Long userId, InvitationStatus status);

    Optional<Invitation> findByWalletIdAndRecipientId(Long walletId, Long userId);
}
