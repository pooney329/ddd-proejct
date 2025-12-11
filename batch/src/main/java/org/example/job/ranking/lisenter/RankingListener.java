package org.example.job.ranking.lisenter;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Objects;

@StepScope
@Component
public class RankingListener implements StepExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    public RankingListener(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();
        Long startIndex = ctx.getLong("startIndex");
        Long endIndex = ctx.getLong("endIndex");


        int deleted = jdbcTemplate.update(
                "DELETE FROM friend_rank WHERE member_idx BETWEEN ? AND ?",
                startIndex, endIndex
        );

        // 로그만 남겨두기
        System.out.printf("[RankDeleteBeforeStepListener] delete rank where member_id between %d and %d (deleted=%d)%n",
                startIndex, endIndex, deleted);
    }
}
