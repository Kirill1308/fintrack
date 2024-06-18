package com.popov.fintrack.user;

import com.popov.fintrack.user.dto.user.ProfileImageDTO;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.membership.model.Member;

public class UserTestData {
    public static final Long USER_ID = 2L;

    public static final User user;
    public static final User userMember;
    public static final UserDTO userDTO;
    public static final Member walletMember;
    public static final ProfileImageDTO profileImageDTO;

    static {
        user = new User();
        user.setId(USER_ID);

        userDTO = new UserDTO();
        userDTO.setId(USER_ID);

        walletMember = new Member();

        userMember = new User();
        userMember.setId(2L);
        walletMember.setUser(userMember);

        profileImageDTO = new ProfileImageDTO();
    }
}
