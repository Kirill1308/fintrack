package com.popov.fintrack.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO representing a range of amounts with a minimum and a maximum value.")
public class AmountRange {

    @Schema(description = "The minimum amount in the range", example = "10.0")
    private Double min;

    @Schema(description = "The maximum amount in the range", example = "1000.0")
    private Double max;
}
