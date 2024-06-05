package com.popov.fintrack.wallet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.popov.fintrack.utills.CurrencyCodeDeserializer;
import com.popov.fintrack.utills.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletDTO {
    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    private Double balance;

    @NotBlank(message = "Currency cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency should be a 3-letter code")
    @JsonDeserialize(using = CurrencyCodeDeserializer.class)
    private String currency;
}
