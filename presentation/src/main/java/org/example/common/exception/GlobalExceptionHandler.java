package org.example.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;
import java.util.StringTokenizer;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * catch로 잡아야하는 Client Exception
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(HttpServletRequest request,
                                                               ClientException e) {
        log.info("ClientException: {}", e.getMessage());
        return makeResponseMessage(request, e.getType(), e.getResponseMessage());
    }

    /**
     * catch로 잡아야하는 Server Exception
     */
    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleServerException(HttpServletRequest request,
                                                               ServerException e) {
        log.error("ServerException: ", e);
        return makeResponseMessage(request, e.getType(), e.getResponseMessage());
    }

    /**
     * 모든 Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
                                                         Exception e) {
        log.error("Exception: ", e);
        return makeResponseMessage(request, ExceptionType.INTERNAL_SERVER_ERROR,
                ExceptionType.INTERNAL_SERVER_ERROR.getText());
    }

    /**
     * 객체 유효성 검사
     */
    @ExceptionHandler(BindException.class)
    ResponseEntity<ErrorResponse> handleObjectValidException(HttpServletRequest request,
                                                             BindException e) {
        log.info("BindException: ", e);

        if (getFirstPath(request).contains("personal")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ErrorResponse.getPersonalFailResult(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                            getBindExceptionMessage(e)));

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.getFailResult(
                            String.valueOf(HttpStatus.BAD_REQUEST.value()),
                            getBindExceptionMessage(e)));

        }
    }

    /**
     * NoResourceFoundException
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
                                                         NoResourceFoundException e) {
        log.warn("NoResourceFoundException: ", e);
        return makeResponseMessage(request, ExceptionType.NOT_FOUND,
                "No resource");
    }

    private ResponseEntity<ErrorResponse> makeResponseMessage(HttpServletRequest request,
                                                              ExceptionType type, String msg) {
        if (getFirstPath(request).contains("personal")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ErrorResponse.getPersonalFailResult(type.getCode(), msg));
        } else {
            return ResponseEntity.status(HttpStatus.valueOf(type.getCode()))
                    .body(ErrorResponse.getFailResult(type.getCode(), msg));
        }
    }

    @ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(HttpServletRequest request, InvalidFormatException e) {
        return makeResponseMessage(request, ExceptionType.BAD_REQUEST, e.getMessage());
    }

    private String getFirstPath(HttpServletRequest request) {
        StringTokenizer tokenizer = new StringTokenizer(request.getRequestURI(), "/", false);
        return tokenizer.nextToken();
    }

    private String getBindExceptionMessage(BindException bindException) {
        try {
            FieldError fieldError = Objects.requireNonNull(bindException.getBindingResult().getFieldError());
            return fieldError.getDefaultMessage();
        } catch (NullPointerException e) {
            return ExceptionType.BAD_REQUEST.getText();
        }
    }

}
