package org.example.job.ranking.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@StepScope
public class ClearFriendRankTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;


    /**
     * 장애가 났을 시 해당 재시작을 위해서 삭제 후 다시 넣는 방법으로 멱등성을 유지한다.
     * 이유는 CursorReader에서 정렬해서 가지고 와서 Processor에서 랭킹을 계산한다.
     * 특정 파티션에서 장애가 나서 재시작하는 경우 동일한 정렬 순서를 보장하지 못한다 친구가 추가
     * 되었거나 그사이에 스코어가 변경되었거나 이경우 결국 이어서 진행 했을 경우 랭킹이 달라져 보장해주지 못해멱등성을 보장해주지 못하기 때문에
     * 재시작이 아닌 삭제해서 다시 넣는 방법이 멱등성 유지에 더 좋다.
     *
     * 또한 데드락 발생을 없앨 수 있다. writer에서 duplicate key update를 사용해서 기존 테이블에 데이터가 존재하여 업데이트 해야하는 경우
     * 데드락이 발생 특히 pk, unique가 있는 경우 우선 유니크 인덱스에 락걸고 인덱스 레코드가 가리키는 pk레코드에 락(범위락 포함)을 건다.
     * 다른 파티션에도 동일 작업진행 하게 되면 동일학 락을 걸면서 각 파티션에서는 많은 락을 걸게되면서 복잡해지고 데드락이 발생
     *
     * > 여기에 병렬 파티션 5개가 동시에 같은 friend_rank를 UPDATE 하니:
     *
     * T1: Unique 인덱스의 일부 페이지 + 그에 대응하는 PK row 잠금
     * T2: 다른 Unique 페이지 + 그에 대응하는 PK row 잠금
     *
     * 인덱스 페이지/PK row 배치에 따라
     * T1이 가지고 있는 락을 T2가 필요로 하고,
     * T2가 가지고 있는 락을 T1이 필요로 하는 상태가 만들어짐
     *
     *
     *
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int count = jdbcTemplate.update("DELETE FROM friend_rank");
        log.info("Deleted {} rows from friend_rank before ranking job.", count);
        return RepeatStatus.FINISHED;
    }
}
