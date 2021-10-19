package com.hw.payAPI.model;

import lombok.Data;


@Data
public class Cancels {
    private String unique_id;
    private String cancelstr;
    private int cost;
    private int tax;
    private String origin_id;
}
