package com.popov.fintrack.email;

import com.popov.fintrack.user.model.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType mailType, Properties params);
}
