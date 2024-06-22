package com.popov.fintrack.user.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "Data Transfer Object for Profile Image Upload")
public class ProfileImageDTO {

    @Schema(description = "Profile image file")
    @NotNull(message = "Image cannot be null")
    private MultipartFile file;
}
