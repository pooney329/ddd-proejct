package org.example.common.exception;

import lombok.Getter;
import org.example.common.ExceptionType;

@Getter
public class ClientException extends RuntimeException {

    private final ExceptionType type;
    private final String responseMessage;

    public ClientException(ExceptionType type) {
        super(type.getText());
        this.type = type;
        this.responseMessage = type.getText();
    }

    public ClientException(ExceptionType type, String message) {
        super(message);
        this.type = type;
        this.responseMessage = message;
    }

    public ClientException(ExceptionType type, String message, String responseMessage) {
        super(message);
        this.type = type;
        this.responseMessage = responseMessage;
    }

    public ClientException(ExceptionType type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
        this.responseMessage = message;
    }


    public ClientException(String message) {
        super(message);
        this.type = ExceptionType.BAD_REQUEST;
        this.responseMessage = message;
    }
}
