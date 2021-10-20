package com.hw.payAPI.dto;

import lombok.Data;

@Data
public class GetInfoDTO {
    private String uid;
    private String maskedCardNum;
    private String validDate;
    private String cvc;

    private String header;
    private String cost;
    private String tax;
}
