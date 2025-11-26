package org.example.common.exception;

import lombok.Getter;
import org.example.common.ExceptionType;

@Getter
public class ServerException extends RuntimeException {

    private final ExceptionType type;
    private final String responseMessage;

    public ServerException(ExceptionType type) {
        super(type.getText());
        this.type = type;
        this.responseMessage = type.getText();
    }

    public ServerException(ExceptionType type, String message) {
        super(message);
        this.type = type;
        this.responseMessage = message;
    }

    public ServerException(String message) {
        super(message);
        this.type = ExceptionType.INTERNAL_SERVER_ERROR;
        this.responseMessage = message;
    }
}

