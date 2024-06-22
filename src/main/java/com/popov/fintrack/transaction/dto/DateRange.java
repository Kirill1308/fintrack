package com.popov.fintrack.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "DTO representing a range of dates with a start date and an end date.")
public class DateRange {

    @NotNull(message = "Start date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "Start date must be in the past or the present")
    @Schema(description = "The start date of the range", example = "2023-01-01")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "End date must be in the future or the present")
    @Schema(description = "The end date of the range", example = "2023-12-31")
    private LocalDate endDate;
}
