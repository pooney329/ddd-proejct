package org.example.job.apijob;

import org.example.job.apijob.dto.MyItem;
import org.example.job.apijob.reader.ExternalApiItemReader;
import org.example.job.apijob.wrtier.LoggingMyItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExternalApiFetchStepConfig {

    @Bean
    public Step externalApiFetchStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ExternalApiItemReader reader,
            LoggingMyItemWriter writer
    ) {
        return new StepBuilder("externalApiFetchStep", jobRepository)
                .<MyItem, MyItem>chunk(50, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }
}
