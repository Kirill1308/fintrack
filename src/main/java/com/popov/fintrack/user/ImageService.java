package com.popov.fintrack.user;

import com.popov.fintrack.user.model.ProfileImage;

public interface ImageService {
    String upload(ProfileImage profileImage);

    void delete(String file);
}
