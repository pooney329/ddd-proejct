package org.example.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final boolean result = false;
    private FailResponse fail;
    private Map<String, Object> data;
    private List<ErrorField> fields;

    public static ErrorResponse getFailResult(String code, String msg) {
        return new ErrorResponse(code, msg);
    }

    public static ErrorResponse getFailResult(String code, String msg, Map<String, Object> data) {
        return new ErrorResponse(code, msg, data);
    }


    public static ErrorResponse getFailResult(String code, String msg, List<ErrorField> fields) {
        return new ErrorResponse(code, msg, fields);
    }

    public static ErrorResponse getPersonalFailResult(String code, String msg) {
        FailResponse failResponse = FailResponse.builder()
                .code(code)
                .msg(msg)
                .build();
        return new ErrorResponse(failResponse);

    }

    public ErrorResponse(String code, String msg, List<ErrorField> fields) {
        this.fail = new FailResponse(code, msg);
        this.fields = fields;
    }

    public ErrorResponse(String code, String msg, Map<String, Object> data) {
        this.fail = new FailResponse(code, msg);
        this.data = data;
    }

    public ErrorResponse(String code, String msg) {
        this.fail = new FailResponse(code, msg);
    }

    public ErrorResponse(FailResponse fail) {
        this.fail = fail;
    }

    public String toStream() {
        return "{" +
                "\"result\":" + "\"" + result + "\"," +
                "\"fail.code\":" + "\"" + (fail != null ? fail.getCode() : "") + "\"," +
                "\"fail.msg\":" + "\"" + (fail != null ? fail.getMsg() : "") + "\"" +
                "}";

    }

    public static class ErrorField {
        private final String message;
        private final String field;

        public String getMessage() {
            return message;
        }

        public String getField() {
            return field;
        }

        ErrorField(Throwable throwable, String field) {
            this(throwable.getMessage(), field);
        }

        ErrorField(String message, String field) {
            this.message = message;
            this.field = field;
        }

        public ErrorField(FieldError fieldError) {
            this(fieldError.getDefaultMessage(), fieldError.getField());
        }
    }

}
