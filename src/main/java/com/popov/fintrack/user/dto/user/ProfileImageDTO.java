package com.popov.fintrack.user.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileImageDTO {

    @NotNull(message = "Image cannot be null")
    private MultipartFile file;
}
