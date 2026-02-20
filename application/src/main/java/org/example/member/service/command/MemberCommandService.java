package org.example.member.service.command;

import lombok.RequiredArgsConstructor;
import org.example.mamber.MemberEntity;
import org.example.mamber.port.MemberRedisRepository;
import org.example.mamber.port.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final MemberRedisRepository memberRedisRepository;



    @Transactional
    public Long join(String name) {
        MemberEntity member = MemberEntity.create(name);
        return memberRepository.save(member).getMemberIdx();
    }

    @Transactional
    public void saveMember(){
        memberRedisRepository.saveMember("pooney_key","pooney_value");
    }


}
