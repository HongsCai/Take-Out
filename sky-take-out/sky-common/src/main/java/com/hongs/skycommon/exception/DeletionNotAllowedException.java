package com.hongs.skycommon.exception;

public class DeletionNotAllowedException extends RuntimeException {
    public DeletionNotAllowedException() {

    }

    public DeletionNotAllowedException(String message) {
        super(message);
    }
}
