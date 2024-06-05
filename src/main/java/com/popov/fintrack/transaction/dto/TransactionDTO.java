package com.popov.fintrack.transaction.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.popov.fintrack.utills.CurrencyCodeDeserializer;
import com.popov.fintrack.validation.OnUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TransactionDTO {
    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "WalletId cannot be null")
    private Long walletId;

    @NotNull(message = "Type cannot be null")
    @Pattern(regexp = "^(INCOME|EXPENSE)$", message = "Type should be either INCOME or EXPENSE")
    private String type;

    @NotBlank(message = "Category cannot be blank")
    @Pattern(regexp = "^[A-Za-z_]+$", message = "Category can only contain letters and underscores")
    private String category;

    @NotBlank(message = "Payment cannot be blank")
    @Pattern(regexp = "^[A-Za-z_]+$", message = "Payment can only contain letters and underscores")
    private String payment;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 9, fraction = 2, message = "Amount precision is up to 9 digits with optional 2 fractional digits")
    private Double amount;

    @NotBlank(message = "Currency cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency should be a 3-letter code")
    @JsonDeserialize(using = CurrencyCodeDeserializer.class)
    private String currency;

    @Size(max = 250, message = "Notes should not exceed 250 characters")
    private String note;

    @NotNull(message = "Date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateCreated;
}
