package org.example.member.controller;


import lombok.RequiredArgsConstructor;
import org.example.member.dto.MemberJoinRequest;
import org.example.member.service.command.MemberCommandService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberCommandController {

    private final MemberCommandService memberService;



    @PostMapping("/join")
    public Long join(@RequestBody MemberJoinRequest memberJoinCommand) {
        return memberService.join(memberJoinCommand.name());
    }

    @PostMapping("/redis")
    public Long saveMember() {
        memberService.saveMember();
        return 1L;
    }


}
