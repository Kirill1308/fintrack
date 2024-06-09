package com.popov.fintrack.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popov.fintrack.user.model.Gender;
import com.popov.fintrack.utills.validation.OnCreate;
import com.popov.fintrack.utills.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Schema(description = "Data Transfer Object for User")
public class UserDTO {

    @Schema(description = "ID of the user", example = "1")
    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    @NotNull(message = "Name cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "Username of the user", example = "johndoe")
    @NotNull(message = "Username cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 3, max = 50, message = "Username must be between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Schema(description = "Phone number of the user", example = "1234567890")
    @Pattern(regexp = "(^$|\\d{10})")
    private String phoneNumber;

    @Schema(description = "Date of birth of the user", example = "1990-01-01")
    @Past
    @Pattern(regexp = "(^$|\\d{4}-\\d{2}-\\d{2})")
    private LocalDate dateOfBirth;

    @Schema(description = "Password of the user", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password cannot be null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(description = "Password confirmation", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation cannot be null", groups = {OnCreate.class})
    private String passwordConfirm;

    @Schema(description = "Gender of the user", example = "MALE")
    @NotNull
    private Gender gender;

    @Schema(description = "File associated with the user", example = "profile.png", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String file;
}
