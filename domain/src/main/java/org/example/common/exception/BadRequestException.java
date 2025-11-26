package org.example.common.exception;

import org.example.common.ExceptionType;

public class BadRequestException extends ClientException {

    public BadRequestException() {
        super(ExceptionType.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(ExceptionType.BAD_REQUEST, message);
    }

    public BadRequestException(String message, String responseMessage) {
        super(ExceptionType.BAD_REQUEST, message, responseMessage);
    }
}
