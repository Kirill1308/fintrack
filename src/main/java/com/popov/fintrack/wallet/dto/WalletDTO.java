package com.popov.fintrack.wallet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.popov.fintrack.utills.CurrencyCodeDeserializer;
import com.popov.fintrack.utills.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data Transfer Object for Wallet")
public class WalletDTO {

    @Schema(description = "Unique identifier of the Wallet", example = "1")
    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Name of the Wallet", example = "My Wallet")
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @Schema(description = "Current balance of the Wallet", example = "1000.00")
    private Double balance;

    @Schema(description = "Currency code of the Wallet", example = "USD")
    @NotBlank(message = "Currency cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency should be a 3-letter code")
    @JsonDeserialize(using = CurrencyCodeDeserializer.class)
    private String currency;
}
