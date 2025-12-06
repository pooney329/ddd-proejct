package org.example.job.ranking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class FriendScoreRow {
    private Long memberIdx;
    private Long friendIdx;
    private Integer score;
}
