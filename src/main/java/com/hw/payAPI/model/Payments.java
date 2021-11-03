package com.hw.payAPI.model;

import lombok.Data;

@Data
public class Payments {
    private int id;
    private String unique_id;
    private String payStr;


    public int getId() {
        return id;
    }

    public String getPayStr(){
        return payStr;
    }

    public String getUnique_id() {return unique_id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setPayStr(String payStr){
        this.payStr = payStr;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }
}
