package org.example.job.ranking;

import lombok.RequiredArgsConstructor;
import org.example.job.ranking.dto.FriendRanking;
import org.example.job.ranking.dto.FriendScoreRow;
import org.example.job.ranking.partition.FriendRankingPartitioner;
import org.example.job.ranking.processor.FriendRankingProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FriendRankingJobConfig {

    private final int CHUNK_SIZE = 2000;

    @Bean
    public Job friendRankingJob(
            JobRepository jobRepository,
            Step friendRankingMasterStep
    ) {
        return new JobBuilder("friendRankingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(friendRankingMasterStep)
                .build();
    }


    @Bean
    public Step friendRankingMasterStep(
            JobRepository jobRepository,
            FriendRankingPartitioner friendRankingPartitioner,
            PartitionHandler friendRankingPartitionHandler
    ) {
        return new StepBuilder("friendRankingMasterStep", jobRepository)
                .partitioner("friendRankWorkerStep", friendRankingPartitioner)
                .partitionHandler(friendRankingPartitionHandler)
                .build();
    }

    @Bean
    public Step friendRankingWorkerStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            JdbcCursorItemReader<FriendScoreRow> friendScoreCursorReader,
            FriendRankingProcessor friendRankingProcessor,
            JdbcBatchItemWriter<FriendRanking> friendRankJdbcBatchItemWriter
    ) {
        return new StepBuilder("friendRankingWorkerStep", jobRepository)
                .<FriendScoreRow, FriendRanking>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(friendScoreCursorReader)
                .processor(friendRankingProcessor)
                .writer(friendRankJdbcBatchItemWriter)
                .build();

    }

    @Bean
    public PartitionHandler friendRankingPartitionHandler(
            @Qualifier("friendRankingWorkerStep") Step friendRankingWorkerStep,
            @Qualifier("friendRankingExecutor") TaskExecutor friendRankingExecutor
    ) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(friendRankingWorkerStep);
        handler.setTaskExecutor(friendRankingExecutor);
        handler.setGridSize(5);
        return handler;
    }

    @Bean
    public TaskExecutor friendRankingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("friend-rank-");
        executor.initialize();
        return executor;
    }
}
