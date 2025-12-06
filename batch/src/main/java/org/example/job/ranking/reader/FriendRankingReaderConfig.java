package org.example.job.ranking.reader;

import org.example.job.ranking.dto.FriendScoreRow;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FriendRankingReaderConfig {
    private final static String sql = """
            
                        (
                            SELECT
                                f.member_idx AS memberIdx,
                                f.friend_idx AS friendIdx,
                                COALESCE(s.score, 0) AS score
                            FROM friend f
                            LEFT JOIN score s
                                ON s.member_idx = f.friend_idx
                            WHERE f.member_idx BETWEEN ? AND ?
                        )
                        UNION ALL
                        (
                            SELECT
                                m.member_idx AS memberIdx,
                                m.member_idx AS friendIdx,
                                COALESCE(s.score, 0) AS score
                            FROM member m
                            LEFT JOIN score s
                                ON s.member_idx = m.member_idx
                            WHERE m.member_idx BETWEEN ? AND ?
                        )
                        ORDER BY
                            memberIdx,
                            score DESC,
                            friendIdx
                        
            """;

    @StepScope
    @Bean
    public JdbcCursorItemReader<FriendScoreRow> friendScoreCursorReader(
            DataSource dataSource,
            @Value("#{stepExecutionContext['startIndex']}") Long startIndex,
            @Value("#{stepExecutionContext['endIndex']}") Long endIndex
    ){
        return new JdbcCursorItemReaderBuilder<FriendScoreRow>()
                .dataSource(dataSource)
                .name("friendScoreCursorReader")
                .sql(sql)
                .rowMapper((rs, rowNum) -> {
                    return FriendScoreRow.builder()
                            .friendIdx(rs.getLong("friendIdx"))
                            .memberIdx(rs.getLong("memberIdx"))
                            .score(rs.getInt("score"))
                            .build();
                })
                .preparedStatementSetter(ps -> {
                    ps.setLong(1, startIndex);
                    ps.setLong(2, endIndex);
                    ps.setLong(3, startIndex);
                    ps.setLong(4, endIndex);
                })
                .build();
    }
}
