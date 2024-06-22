package com.popov.fintrack.email.listener;

import com.popov.fintrack.config.AppProperties;
import com.popov.fintrack.email.MailService;
import com.popov.fintrack.email.MailType;
import com.popov.fintrack.email.event.ExclusionEvent;
import com.popov.fintrack.email.event.InvitationConfirmEvent;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@AllArgsConstructor
public class MailListeners {

    private final AppProperties appProperties;

    private final MailService mailService;
    private final UserService userService;
    private final WalletService walletService;

    @EventListener
    public void confirmInvitation(InvitationConfirmEvent event) {
        String confirmationUrl = appProperties.getHostUrl() + "/api/v1/wallets/membership/accept/?token=" + event.token();
        User recipient = userService.getByUsername(event.invitation().getRecipientEmail());

        Properties mailProperties = new Properties();
        mailProperties.put("sender", event.invitation().getSenderId().toString());
        mailProperties.put("invitationLink", confirmationUrl);

        mailService.sendEmail(recipient, MailType.INVITATION, mailProperties);
        mailService.sendEmail(recipient, MailType.INVITATION, mailProperties);
    }

    @EventListener
    public void handleExclusion(ExclusionEvent event) {
        User recipient = userService.getUserById(event.userId());
        Wallet wallet = walletService.getWalletById(event.walletId());

        Properties mailProperties = new Properties();
        mailProperties.put("walletName", wallet.getName());
        mailService.sendEmail(recipient, MailType.EXCLUSION, mailProperties);
    }

}
