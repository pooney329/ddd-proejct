package org.example.member.repository;

import org.example.mamber.MemberEntity;
import org.example.mamber.MemberSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberJpaCustom {
    Page<MemberEntity> search(MemberSearchCondition condition, Pageable pageable);
}
