
package org.example.point.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mamber.MemberEntity;
import org.hibernate.annotations.Comment;

@Comment(value = "포인트")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        catalog = "common_db",
        name = "point",
        uniqueConstraints = {},
        indexes = {}
)
@Entity
public class Point {

    @Id
    @Column(name = "point_idx", nullable = false)
    private Long pointIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    private MemberEntity member;

    @Column(name = "point", nullable = false)
    private Integer point;
}
