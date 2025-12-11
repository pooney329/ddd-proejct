package org.example.job.ranking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class FriendRanking {
    private Long memberIdx;
    private Long friendIdx;
    private Integer score;
    private Integer ranking;
}
