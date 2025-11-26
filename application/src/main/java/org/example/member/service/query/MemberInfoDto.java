package org.example.member.service.query;

import lombok.Builder;
import org.example.mamber.MemberEntity;

@Builder
public record MemberInfoDto(
        Long memberIdx,
        String name
) {
    public static MemberInfoDto from(MemberEntity member){
        return MemberInfoDto
                .builder()
                .memberIdx(member.getMemberIdx())
                .name(member.getName())
                .build();
    }
}
