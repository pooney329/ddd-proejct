package org.example.mamber;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.common.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum MemberStatus implements BaseEnum<MemberStatus> {
    ACTIVE(1, "활성화"),
    INACTIVE(2, "비활성화");


    @JsonValue
    private final Integer value;
    private final String text;
}
