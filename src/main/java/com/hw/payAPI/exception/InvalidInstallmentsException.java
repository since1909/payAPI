package com.hw.payAPI.exception;

public class InvalidInstallmentsException extends RuntimeException {
    public InvalidInstallmentsException(String msg, Throwable t) {
        super(msg, t);
    }
    public InvalidInstallmentsException(String msg) {
        super(msg);
    }
    public InvalidInstallmentsException() {
        super();
    }
}
