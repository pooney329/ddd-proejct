package org.example.member.service.query;

import lombok.RequiredArgsConstructor;
import org.example.mamber.MemberEntity;
import org.example.mamber.MemberSearchCondition;
import org.example.mamber.port.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;




    public Page<MemberInfoDto> searchMember(MemberSearchQuery query) {
        MemberSearchCondition searchCondition = new MemberSearchCondition(query.name());
        PageRequest pageRequest = PageRequest.of(query.page(), query.pageSize());
        return memberRepository.search(searchCondition, pageRequest)
                .map(MemberInfoDto::from);
    }


}
