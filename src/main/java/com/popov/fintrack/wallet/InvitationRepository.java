package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByToken(String token);

    Optional<Invitation> findByWalletIdAndRecipientId(Long walletId, Long userId);
}
