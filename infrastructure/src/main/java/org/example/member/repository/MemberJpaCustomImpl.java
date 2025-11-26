package org.example.member.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.mamber.MemberEntity;
import org.example.mamber.MemberSearchCondition;
import org.example.mamber.QMemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static org.example.mamber.QMemberEntity.*;


@RequiredArgsConstructor
public class MemberJpaCustomImpl implements MemberJpaCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<MemberEntity> search(MemberSearchCondition condition, Pageable pageable) {
        List<MemberEntity> content = jpaQueryFactory
                .selectFrom(memberEntity)
                .where(memberEntity.name.eq(condition.name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberEntity.memberIdx.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(memberEntity.count())
                .from(memberEntity)
                .where(memberEntity.name.eq(condition.name()));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
