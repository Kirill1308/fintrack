package com.popov.fintrack.user;

import com.popov.fintrack.user.dto.user.ProfileImageDTO;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.Gender;
import com.popov.fintrack.user.model.User;

import java.time.LocalDate;

public class UserTestData {

    public static final Long ADMIN_ID = 1L;
    public static final Long USER1_ID = 2L;
    public static final Long USER2_ID = 3L;

    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String USER_MAIL = "user1@gmail.com";
    public static final String WALLET_MEMBER_MAIL = "user2@gmail.com";

    public static final User admin;
    public static final User user;
    public static final User walletMember;

    public static final UserDTO userDTO;
    public static final ProfileImageDTO profileImageDTO;

    static {
        admin = new User();
        admin.setId(ADMIN_ID);
        admin.setName("adminName");
        admin.setUsername(ADMIN_MAIL);
        admin.setPassword("password");
        admin.setPhoneNumber("1234567890");
        admin.setDateOfBirth(LocalDate.parse("1990-01-01"));
        admin.setFile("profile.jpg");
        admin.setGender(Gender.MALE);

        user = new User();
        user.setId(USER1_ID);
        user.setName("userName1");
        user.setUsername(USER_MAIL);
        user.setPassword("password");
        user.setPhoneNumber("0987654321");
        user.setDateOfBirth(LocalDate.parse("1992-02-02"));
        user.setFile("profile2.jpg");
        user.setGender(Gender.FEMALE);

        userDTO = new UserDTO();
        userDTO.setId(USER1_ID);

        walletMember = new User();
        walletMember.setId(USER2_ID);
        walletMember.setName("userName2");
        walletMember.setUsername(WALLET_MEMBER_MAIL);
        walletMember.setPassword("password");
        walletMember.setPhoneNumber("0987654321");
        walletMember.setDateOfBirth(LocalDate.parse("1992-02-02"));
        walletMember.setFile("profile2.jpg");
        walletMember.setGender(Gender.MALE);

        profileImageDTO = new ProfileImageDTO();
    }
}
