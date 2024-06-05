package com.popov.fintrack.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popov.fintrack.user.model.Gender;
import com.popov.fintrack.validation.OnCreate;
import com.popov.fintrack.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class UserDTO {

    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "Name cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Username cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 3, max = 50, message = "Username must be between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Pattern(regexp = "(^$|\\d{10})")
    private String phoneNumber;

    @Past
    @Pattern(regexp = "(^$|\\d{4}-\\d{2}-\\d{2})")
    private LocalDate dateOfBirth;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password cannot be null", groups = {OnCreate.class, OnUpdate.class})
    //@Pattern(regexp = ...) // if you want to validate password pattern eg. one symbol, one uppercase, one lowercase etc
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation cannot be null", groups = {OnCreate.class})
    //@Pattern(regexp =...) // same as password
    private String passwordConfirm;

    @NotNull
    private Gender gender;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String file;
}
