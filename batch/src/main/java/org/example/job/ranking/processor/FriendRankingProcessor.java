package org.example.job.ranking.processor;

import org.example.job.ranking.dto.FriendRanking;
import org.example.job.ranking.dto.FriendScoreRow;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@StepScope
@Component
public class FriendRankingProcessor implements ItemProcessor<FriendScoreRow, FriendRanking> {

    private Long currentMemberIdx = null;
    private Integer currentRanking = null;
    private Integer currentScore = null;


    @Override
    public FriendRanking process(FriendScoreRow item) {
        Long memberIdx = item.getMemberIdx();
        ;
        Long friendIdx = item.getFriendIdx();
        ;

        //신규 진입 이거나 member가 바뀐 경우 랭킹 조기화 진행
        if (Objects.isNull(currentMemberIdx) || !currentMemberIdx.equals(memberIdx)) {
            currentMemberIdx = memberIdx;
            currentRanking = 1;
            currentScore = item.getScore();
        } else {
            if (!Objects.equals(currentScore, item.getScore())) {
                currentRanking++;
                currentScore = item.getScore();
            }
        }

        return FriendRanking.builder()
                .memberIdx(memberIdx)
                .friendIdx(item.getFriendIdx())
                .score(item.getScore())
                .ranking(currentRanking)
                .build();
    }
}
