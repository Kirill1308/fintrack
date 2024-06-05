package com.popov.fintrack.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileImage {

    private MultipartFile file;
}
