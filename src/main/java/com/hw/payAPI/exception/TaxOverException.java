package com.hw.payAPI.exception;

public class TaxOverException extends RuntimeException {
    public TaxOverException(String msg, Throwable t) {
        super(msg, t);
    }
    public TaxOverException(String msg) {
        super(msg);
    }
    public TaxOverException() {
        super();
    }
}

