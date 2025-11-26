package org.example.member;

import org.example.member.dto.MemberResponse;
import org.example.member.dto.MemberSearchRequest;
import org.example.member.service.query.MemberInfoDto;
import org.example.member.service.query.MemberSearchQuery;
import org.springframework.stereotype.Component;


public class MemberMapper {


    public MemberSearchQuery toQuery(MemberSearchRequest request) {
        return new MemberSearchQuery(request.name(), request.page(), request.pageSize());
    }

    public MemberResponse toResponse(MemberInfoDto dto) {
        return new MemberResponse(dto.memberIdx(), dto.name());
    }
}
