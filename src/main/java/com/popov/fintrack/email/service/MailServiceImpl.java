package com.popov.fintrack.email.service;

import com.popov.fintrack.email.MailService;
import com.popov.fintrack.email.MailType;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.model.User;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(User user, MailType mailType, Properties params) {
        switch (mailType) {
            case REGISTRATION -> sendRegistrationEmail(user, params);
            case INVITATION -> sendInvitationEmail(user, params);
            case EXCLUSION -> sendExclusionEmail(user, params);
            default -> throw new ResourceNotFoundException("Unsupported mail type");
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(User user, Properties params) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject("Thank you for registration, " + user.getName());
        helper.setTo(user.getUsername());
        String emailContent = getRegistrationEmailContent(user, params);
        helper.setText(emailContent, true);
        emailSender.send(message);
    }

    @SneakyThrows
    private void sendInvitationEmail(User user, Properties params) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject("You have been invited to join wallet");
        helper.setTo(user.getUsername());
        String emailContent = getInvitationEmailContent(user, params);
        helper.setText(emailContent, true);
        emailSender.send(message);
    }

    @SneakyThrows
    private void sendExclusionEmail(User user, Properties params) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject("You have been excluded from the wallet");
        helper.setTo(user.getUsername());
        String emailContent = getExclusionEmailContent(user, params);
        helper.setText(emailContent, true);
        emailSender.send(message);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(User user, Properties params) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        configuration.getTemplate("registration.ftlh").process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getInvitationEmailContent(User user, Properties params) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("sender", params.getProperty("sender"));
        model.put("invitationLink", params.getProperty("invitationLink"));
        configuration.getTemplate("invitation.ftlh").process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getExclusionEmailContent(User user, Properties params) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("walletName", params.getProperty("walletName"));
        configuration.getTemplate("exclusion.ftlh").process(model, writer);

        return writer.getBuffer().toString();
    }
}
