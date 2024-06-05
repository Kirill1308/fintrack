package com.popov.fintrack.wallet.service;

import com.popov.fintrack.user.UserService;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.InvitationRepository;
import com.popov.fintrack.wallet.InvitationService;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.dto.InvitationRequest;
import com.popov.fintrack.wallet.model.Invitation;
import com.popov.fintrack.wallet.model.InvitationStatus;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final WalletService walletService;
    private final UserService userService;
    private final InvitationRepository invitationRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Invitation> findByToken(String token) {
        return invitationRepository.findByToken(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invitation> findByWalletId(Long walletId) {
        return invitationRepository.findByWalletIdAndStatus(walletId, InvitationStatus.ACCEPTED);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invitation> findByWalletIdAndRecipientId(Long walletId, Long userId) {
        return invitationRepository.findByWalletIdAndRecipientId(walletId, userId);
    }

    @Override
    @Transactional
    public Invitation saveInvitation(InvitationRequest request) {
        Wallet wallet = walletService.getWalletById(request.getWalletId());
        User sender = userService.getUserById(request.getSenderId());
        User recipient = userService.getByUsername(request.getRecipientEmail());

        Optional<Invitation> existingInvitation = invitationRepository.findByWalletIdAndRecipientId(request.getWalletId(), recipient.getId());
        if (existingInvitation.isPresent()) {
            throw new IllegalStateException("An invitation has already been sent to this user for this wallet.");
        }

        Invitation invitation = new Invitation();
        invitation.setWallet(wallet);
        invitation.setSender(sender);
        invitation.setRecipient(recipient);
        invitation.setStatus(InvitationStatus.PENDING);

        invitationRepository.save(invitation);

        return invitation;
    }

    @Override
    @Transactional
    public void updateInvitation(Invitation invitation) {
        invitationRepository.save(invitation);
    }

    @Override
    @Transactional
    public void deleteInvitation(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invitation> getAcceptedInvitations(Long userId) {
        return invitationRepository.findByRecipientIdAndStatus(userId, InvitationStatus.ACCEPTED);
    }
}
