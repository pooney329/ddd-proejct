package org.example.job.ranking.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.job.ranking.dto.FriendRanking;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class FriendRankDeletionWriter implements ItemWriter<FriendRanking> {

    private final JdbcTemplate jdbcTemplate;

    @Value("#{stepExecutionContext['startIndex']}")
    private Long startIndex;

    @Value("#{stepExecutionContext['endIndex']}")
    private Long endIndex;

    private boolean deleted = false;

    @Override
    public void write(Chunk<? extends FriendRanking> items) throws Exception {
        // 이 로직은 해당 Worker Step의 첫 번째 Chunk 트랜잭션에서만 실행되도록 보장해야 합니다.
        if (!deleted) {
            String sql = "DELETE FROM friend_rank WHERE member_idx BETWEEN ? AND ?";
            int count = jdbcTemplate.update(sql, startIndex, endIndex);

            // 첫 번째 Chunk에서만 삭제가 이루어지도록 플래그 설정
            deleted = true;
            log.info("Partition Range [{}, {}]: Deleted {} existing friend_rank records.",
                    startIndex, endIndex, count);
        }
    }
}
