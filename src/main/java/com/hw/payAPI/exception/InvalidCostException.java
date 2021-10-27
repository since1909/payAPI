package com.hw.payAPI.exception;

public class InvalidCostException extends RuntimeException {
    public InvalidCostException(String msg, Throwable t) {
        super(msg, t);
    }
    public InvalidCostException(String msg) {
        super(msg);
    }
    public InvalidCostException() {
        super();
    }
}
