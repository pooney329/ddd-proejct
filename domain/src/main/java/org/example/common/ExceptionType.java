package org.example.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionType {

    INTERNAL_SERVER_ERROR("500", "서버에서 에러가 발생했습니다.(관리자 문의)"),

    BAD_REQUEST("400", "잘못된 요청입니다."),
    ENUM_NOT_FOUND("404", "선택 값이 없습니다."),
    CONFLICT("409", "중복된 값이 존재합니다."),


    NOT_FOUND("404", "대상이 없습니다.");

    private final String code;
    private final String text;

}
