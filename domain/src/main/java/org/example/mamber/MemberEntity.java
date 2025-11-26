package org.example.mamber;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Comment(value = "회원")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        catalog = "common_db",
        name = "member",
        uniqueConstraints = {},
        indexes = {}
)
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_idx", nullable = false)
    private Long memberIdx;

    @Column(name = "name", nullable = false)
    private String name;


    @Builder(access = AccessLevel.PRIVATE)
    private MemberEntity(String name) {
        this.name = name;
    }

    public static MemberEntity create(String name) {
        return MemberEntity.builder()
                .name(name)
                .build();
    }
}
