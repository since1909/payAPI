package com.hw.payAPI.exception;

public class CostOverException extends RuntimeException {
    public CostOverException(String msg, Throwable t) {
        super(msg, t);
    }
    public CostOverException(String msg) {
        super(msg);
    }
    public CostOverException() {
        super();
    }
}