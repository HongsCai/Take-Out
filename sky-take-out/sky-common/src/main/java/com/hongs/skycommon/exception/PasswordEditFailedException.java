package com.hongs.skycommon.exception;

public class PasswordEditFailedException extends RuntimeException {
    public PasswordEditFailedException() {

    }

    public PasswordEditFailedException(String message) {
        super(message);
    }
}
