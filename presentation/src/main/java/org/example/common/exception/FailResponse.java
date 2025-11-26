package org.example.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FailResponse {

    private String code;
    private String msg;

    @Builder
    public FailResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
