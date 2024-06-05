package com.popov.fintrack.user;

import com.popov.fintrack.user.model.ProfileImage;
import com.popov.fintrack.user.model.User;

public interface UserService {
    User getByUsername(String username);

    User getUserById(Long userId);

    User create(User user);

    User update(User user);

    void deleteUserById(Long userId);

    void uploadProfileImage(Long userId, ProfileImage profileImage);

    void deleteProfileImage(Long userId);
}
