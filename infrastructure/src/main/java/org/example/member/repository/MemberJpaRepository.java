package org.example.member.repository;

import org.example.mamber.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> , MemberJpaCustom{
}





