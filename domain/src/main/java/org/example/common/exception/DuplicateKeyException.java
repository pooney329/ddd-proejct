package org.example.common.exception;

import org.example.common.ExceptionType;

public class DuplicateKeyException extends ClientException {

    public DuplicateKeyException() {
        super(ExceptionType.CONFLICT);
    }

    public DuplicateKeyException(String message) {
        super(ExceptionType.CONFLICT, message);
    }

    public DuplicateKeyException(String message, String responseMessage) {
        super(ExceptionType.CONFLICT, message, responseMessage);
    }
}
