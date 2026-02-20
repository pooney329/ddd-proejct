package org.example.mamber.port;

import org.example.mamber.MemberEntity;
import org.example.mamber.MemberSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {
    MemberEntity save(MemberEntity member);
    Page<MemberEntity> search(MemberSearchCondition condition, Pageable pageable);

}
