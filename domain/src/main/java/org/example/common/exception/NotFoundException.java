package org.example.common.exception;

import org.example.common.ExceptionType;

public class NotFoundException extends ClientException {

    public NotFoundException() {
        super(ExceptionType.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(ExceptionType.NOT_FOUND, message);
    }

    public NotFoundException(String message, String responseMessage) {
        super(ExceptionType.NOT_FOUND, message, responseMessage);
    }
}

