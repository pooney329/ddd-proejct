package org.example.common.exception;

import org.example.common.ExceptionType;

public class InternalServerException extends ServerException {

    public InternalServerException() {
        super(ExceptionType.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(String message) {
        super(ExceptionType.INTERNAL_SERVER_ERROR, message);
    }
}

