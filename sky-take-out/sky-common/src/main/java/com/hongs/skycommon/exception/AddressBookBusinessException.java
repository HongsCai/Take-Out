package com.hongs.skycommon.exception;

public class AddressBookBusinessException extends RuntimeException {
    public AddressBookBusinessException() {

    }

    public AddressBookBusinessException(String message) {
        super(message);
    }
}
