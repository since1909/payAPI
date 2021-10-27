package com.hw.payAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Optional;

@Data
public class PayInfoDTO {
    private String id;

    @JsonProperty("cardNum")
    @Size(min = 10, max = 16, message="카드번호는 10자리 이상 16자리 이하로 입력해야 합니다.")
    private String cardNum;

    @JsonProperty("validDate")
    @Length(min = 4, max = 4, message="유효기간은 4자리로 입력해야 합니다.")
    private String validDate;

    @JsonProperty("cvc")
    @Length(min = 3, max = 3, message="CVC 번호는 3자리로 입력해야 합니다.")
    private String cvc;

    @JsonProperty("installments")
    private String installments;

    @JsonProperty("cost")
    private String cost;

    @JsonProperty("tax")
    private Optional<String> tax;
}
