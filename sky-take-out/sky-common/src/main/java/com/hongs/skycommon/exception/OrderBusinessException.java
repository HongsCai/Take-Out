package com.hongs.skycommon.exception;

public class OrderBusinessException extends RuntimeException {
    public OrderBusinessException() {

    }

    public OrderBusinessException(String message) {
        super(message);
    }
}
