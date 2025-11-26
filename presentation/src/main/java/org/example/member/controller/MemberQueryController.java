package org.example.member.controller;


import lombok.RequiredArgsConstructor;
import org.example.member.MemberMapper;
import org.example.member.dto.MemberResponse;
import org.example.member.dto.MemberSearchRequest;
import org.example.member.service.command.MemberCommandService;
import org.example.member.service.query.MemberQueryService;
import org.example.member.service.query.MemberSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;
    private final MemberMapper memberMapper = new MemberMapper();



    @GetMapping
    public Page<MemberResponse> search(@ModelAttribute MemberSearchRequest searchRequest) {
        MemberSearchQuery query = memberMapper.toQuery(searchRequest);
        return memberQueryService.searchMember(query)
                .map(memberMapper::toResponse);
    }


}
