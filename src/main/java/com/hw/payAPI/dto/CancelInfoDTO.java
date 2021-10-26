package com.hw.payAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class CancelInfoDTO {
    @JsonProperty("uid")
    private String unique_id;

    @JsonProperty("cancelCost")
    private String cancelCost;

    @JsonProperty("cancelTax")
    private Optional<String> cancelTax;
}
