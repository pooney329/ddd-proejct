package org.example.job.ranking.writer;

import org.example.job.ranking.dto.FriendRanking;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class FriendRankWriterConfig {

    @Bean
    public JdbcBatchItemWriter<FriendRanking> friendRankJdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<FriendRanking>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO friend_rank (member_idx, friend_idx, score, ranking)
                        VALUES (:memberIdx, :friendIdx, :score, :ranking)
                        ON DUPLICATE KEY UPDATE 
                        score = VALUES(score)
                        """)
                .beanMapped()
                .build();
    }

    @Bean
    public CompositeItemWriter<FriendRanking> compositeWriter(
//            ItemWriter<FriendRanking> friendRankDeletionWriter,
            JdbcBatchItemWriter<FriendRanking> friendRankJdbcBatchItemWriter
    ) {
        CompositeItemWriter<FriendRanking> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(
//                friendRankDeletionWriter,
                friendRankJdbcBatchItemWriter
        ));
        return writer;
    }
}
