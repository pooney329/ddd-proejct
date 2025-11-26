package org.example.member.repository;

import lombok.RequiredArgsConstructor;
import org.example.mamber.MemberEntity;
import org.example.mamber.MemberSearchCondition;
import org.example.mamber.port.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public MemberEntity save(MemberEntity member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Page<MemberEntity> search(MemberSearchCondition condition, Pageable pageable) {
        return memberJpaRepository.search(condition, pageable);
    }
}
