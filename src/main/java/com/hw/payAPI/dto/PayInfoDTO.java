package com.hw.payAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class PayInfoDTO {
    private String id;

    @JsonProperty("cardNum")
    private String cardNum;

    @JsonProperty("validDate")
    private String validDate;

    @JsonProperty("cvc")
    private String cvc;

    @JsonProperty("installments")
    private String installments;

    @JsonProperty("cost")
    private String cost;

    @JsonProperty("tax")
    private Optional<String> tax;
}
