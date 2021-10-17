package com.hw.payAPI.model;

public class Payments {
    private String id;
    private String payStr;

    public Payments(String id, String payStr){
        super();
        this.id = id;
        this.payStr = payStr;
    }

    public String getId() {
        return id;
    }

    public String getPayStr(){
        return payStr;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPayStr(String payStr){
        this.payStr = payStr;
    }
}
