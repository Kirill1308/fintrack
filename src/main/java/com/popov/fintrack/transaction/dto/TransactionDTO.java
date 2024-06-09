package com.popov.fintrack.transaction.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.popov.fintrack.utills.CurrencyCodeDeserializer;
import com.popov.fintrack.utills.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data transfer object for transactions")
public class TransactionDTO {

    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    @Schema(description = "Unique identifier of the transaction", example = "1")
    private Long id;

    @NotNull(message = "Type cannot be null")
    @Pattern(regexp = "^(INCOME|EXPENSE)$", message = "Type should be either INCOME or EXPENSE")
    @Schema(description = "Type of the transaction", example = "INCOME", allowableValues = {"INCOME", "EXPENSE"})
    private String type;

    @NotBlank(message = "Category cannot be blank")
    @Pattern(regexp = "^[A-Za-z_]+$", message = "Category can only contain letters and underscores")
    @Schema(description = "Category of the transaction", example = "GROCERIES")
    private String category;

    @NotBlank(message = "Payment cannot be blank")
    @Pattern(regexp = "^[A-Za-z_]+$", message = "Payment can only contain letters and underscores")
    @Schema(description = "Payment method for the transaction", example = "CARD")
    private String payment;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 9, fraction = 2, message = "Amount precision is up to 9 digits with optional 2 fractional digits")
    @Schema(description = "Amount of the transaction", example = "99.99")
    private Double amount;

    @NotBlank(message = "Currency cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency should be a 3-letter code")
    @JsonDeserialize(using = CurrencyCodeDeserializer.class)
    @Schema(description = "Currency code for the transaction", example = "USD")
    private String currency;

    @Size(max = 250, message = "Notes should not exceed 250 characters")
    @Schema(description = "Additional notes for the transaction", example = "Paid for monthly groceries")
    private String note;

    @NotNull(message = "Date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Date the transaction was created", example = "2023-05-20")
    private LocalDate dateCreated;
}
