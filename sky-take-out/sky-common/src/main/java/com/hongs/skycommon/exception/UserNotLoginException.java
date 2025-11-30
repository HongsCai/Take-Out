package com.hongs.skycommon.exception;

public class UserNotLoginException extends RuntimeException {
    public UserNotLoginException() {

    }

    public UserNotLoginException(String message) {
        super(message);
    }
}
